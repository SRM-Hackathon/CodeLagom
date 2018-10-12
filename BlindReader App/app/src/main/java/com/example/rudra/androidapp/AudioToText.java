package com.example.rudra.androidapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.projects.alshell.vokaturi.EmotionProbabilities;
import com.projects.alshell.vokaturi.Vokaturi;
import com.projects.alshell.vokaturi.VokaturiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

///////////////////////////////bluetooh imports

import java.util.Set;
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


public class AudioToText extends AppCompatActivity {
    int WR_Perm=1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };
    TextView tv;
    Vokaturi vokaturiApi;
    Button fuck;
    Conversion conv;

    String address = null , name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
                    String bin=conv.conversion(i.charAt(j));
                    btSocket.getOutputStream().write(bin.toString().getBytes());
                    Log.d("Rudra",bin.toString().getBytes().toString());
                }

//                btSocket.getOutputStream().write(i.toString().getBytes());
//                Log.d("Rudra",i.toString().getBytes().toString());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_to_text);
        tv = findViewById(R.id.textView);
        fuck = findViewById(R.id.button);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        if ((ContextCompat.checkSelfPermission(AudioToText.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(AudioToText.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(AudioToText.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            Log.d("Ricky", "Already Granted");
        } else {
            requestUserPermission();
        }
        try {
            vokaturiApi = Vokaturi.getInstance(getApplicationContext());
        } catch (VokaturiException e) {
            e.printStackTrace();
        }
        fuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    vokaturiApi.startListeningForSpeech();
                    tv.setText("Listening started \n" + "Speak Something \n");
                    Button temp = findViewById(R.id.button);
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EmotionProbabilities emotionProbabilities;
                            try {
                                emotionProbabilities = vokaturiApi.stopListeningAndAnalyze();

                                if (emotionProbabilities.Neutrality > emotionProbabilities.Anger && emotionProbabilities.Neutrality > emotionProbabilities.Fear && emotionProbabilities.Neutrality > emotionProbabilities.Sadness && emotionProbabilities.Neutrality > emotionProbabilities.Happiness) {
                                    tv.setText("Neutrality");


                                } else if (emotionProbabilities.Anger > emotionProbabilities.Neutrality && emotionProbabilities.Anger > emotionProbabilities.Fear && emotionProbabilities.Anger > emotionProbabilities.Sadness && emotionProbabilities.Anger > emotionProbabilities.Happiness) {
                                    tv.setText("Anger");
                                } else if (emotionProbabilities.Fear > emotionProbabilities.Neutrality && emotionProbabilities.Fear > emotionProbabilities.Anger && emotionProbabilities.Fear > emotionProbabilities.Sadness && emotionProbabilities.Fear > emotionProbabilities.Happiness) {
                                    tv.setText("Fear");
                                } else if (emotionProbabilities.Sadness > emotionProbabilities.Neutrality && emotionProbabilities.Sadness > emotionProbabilities.Anger && emotionProbabilities.Sadness > emotionProbabilities.Fear && emotionProbabilities.Sadness > emotionProbabilities.Happiness) {
                                    tv.setText("Sadness");
                                } else {
                                    tv.setText("Happiness");
                                }


                            } catch (Exception e) {

                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button s2t = findViewById(R.id.button3);
        s2t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSpeechInput();
            }
        });
        try {
            bluetooth_connect_device();
        } catch (Exception e) {
        }

        final Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });



    }

    public void getSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, 10);
        } else {
            Log.d("Ricky", "Fucked Up");
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sendArduino(result.get(0));
                    tv.setText(result.get(0));




                }
                break;

        }

    }
    public void requestUserPermission() {
        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))) {

        } else {
            ActivityCompat.requestPermissions(this,PERMISSIONS,WR_Perm);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WR_Perm){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                //pernission granted
            }else{
                //not granted
            }
        }
    }


}

