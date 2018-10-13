package com.example.rudra.androidapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Directions extends AppCompatActivity {
    Button sayDir,givDir,exit;
    String dirs;
    String[] ar=new String[100];


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

        try {
            if (btSocket != null) {

                Log.d("Arduinomethod", "Inside arduino method");
                String bin = "";
                if (i.equals("left")) {
                    bin = "llllll";
                } else if (i.equals("right")) {
                    bin = "rrrrrr";
                } else if (i.equals("straight")) {
                    bin = "ssssss";
                } else if (i.equals("back")) {
                    bin = "bbbbbb";
                } else if (i.equals("forward")) {
                    bin = "ffffff";
                } else if (i.equals("stop")) {
                    bin = "tttttt";
                }
                bin += ",";
                Log.d("SRMTest",bin);
                btSocket.getOutputStream().write(bin.toString().getBytes());

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
        for(int i=0;i<100;i++){
            ar[i]="ahoy";
        }
        setContentView(R.layout.activity_directions);
        sayDir=findViewById(R.id.sayDir);
        givDir=findViewById(R.id.givDir);
        exit=findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sayDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });
        try{
            bluetooth_connect_device();
        }catch (Exception e){}

        givDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<100;i++){
                    if(!ar[i].equals("ahoy")){
                        sendArduino(ar[i]);
                        Log.d("SRMTest",ar[i]);
                        ar[i]="ahoy";
                        break;
                    }
                }
            }
        });

    }

    public void getSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if(resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    dirs=result.get(0);
                }
                break;
        }
        for(int i=0;i<100;i++){
            if(dirs.indexOf("left")>=0)
            ar[dirs.indexOf("left")]="left";
            if(dirs.indexOf("right")>=0)
            ar[dirs.indexOf("right")]="right";
            if(dirs.indexOf("straight")>=0)
            ar[dirs.indexOf("straight")]="straight";
            if(dirs.indexOf("back")>=0)
            ar[dirs.indexOf("back")]="back";
            if(dirs.indexOf("forward")>=0)
            ar[dirs.indexOf("forward")]="forward";
            if(dirs.indexOf("backward")>=0)
            ar[dirs.indexOf("backward")]="backward";
            if(dirs.indexOf("stop")>=0)
            ar[dirs.indexOf("stop")]="stop";
        }
        //Log.d("SRMTest",dirs.indexOf("left")+" "+dirs.indexOf("right")+" "+dirs.indexOf("mogaan"));
    }
}
