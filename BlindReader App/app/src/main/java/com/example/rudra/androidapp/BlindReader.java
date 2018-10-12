package com.example.rudra.androidapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//////////////////////////////bluetooth imports
import java.util.ArrayList;
import java.util.Locale;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BlindReader extends AppCompatActivity {

    SurfaceView cv;
    Conversion conv;
    TextView tv;
    CameraSource cs;
    final int rcID = 1001;


    String address = null , name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case rcID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cs.start(cv.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    private void bluetooth_connect_device() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();name = bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();

                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        Toast.makeText(this,name+"\n"+address,Toast.LENGTH_LONG).show();
    }

    private void sendArduino(String i)
    {

        try
        {
            if (btSocket!=null)
            {
                for(int j=0;j<i.length();j++){

                    Log.d("Arduinomethod", "Inside arduino method") ;
                    String bin=conv.conversion(i.charAt(j));
                    bin+=",";
                    for(int k=0;k<bin.length();k++){
                        char z=bin.charAt(k);
                        btSocket.getOutputStream().write(Character.toString(z).getBytes());
                    }

                    Log.d("Rudra",bin.toString());
                } }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(BlindReader.this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //<recive from  arduino  code>

    ////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_reader);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        try{
            bluetooth_connect_device();
        }catch(Exception e){}
        cv = findViewById(R.id.surface_view);
        tv = findViewById(R.id.text_view);



        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("BlindReader", "Detector dependencies are not yet available");
        } else {

            cs = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cv.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(BlindReader.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    rcID);
                            return;
                        }
                        cs.start(cv.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cs.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        tv.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    final String send_string=item.getValue();
                                    sendArduino(send_string);
                                    Log.d("Rudra","Sending :"+send_string);
                                    //send text to method to transfer to bluetooth
                                    stringBuilder.append("\n");
                                }
                                tv.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
        final Button back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                back_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(BlindReader.this, MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        });

    }
    //method to transfer to bluetooth
    //convert each char to binary string and send
}
