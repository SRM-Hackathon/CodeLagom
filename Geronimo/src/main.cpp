#include <Arduino.h>
#include <Servo.h>
#include <string.h>

Servo sl1,sl2,sl3,sr1,sr2,sr3;
int led0=13,led1=12,led2=11,led3=10,led4=9,led5=8;
String data="";

void setup() {
    Serial.begin(9600);
    sl1.attach(5);
    sl2.attach(6);
    sl3.attach(7);
    sr1.attach(4);
    sr2.attach(3);
    sr3.attach(2);
   pinMode(led0,OUTPUT);
   pinMode(led1,OUTPUT);
   pinMode(led2,OUTPUT);
   pinMode(led3,OUTPUT);
   pinMode(led4,OUTPUT);
   pinMode(led5,OUTPUT);
   sl1.write(0);
   sl2.write(0);
   sl3.write(0);
   sr1.write(0);
   sr2.write(0);
   sr3.write(100);
}

void loop() {

    if(Serial.available()){
        char ch=Serial.read();
        //Serial.println(ch);
        if(ch == ','){
            Serial.println(data);
            data="";
        }else{
            data+=ch;
        }
        //Serial.println(data);
        //Serial.println(mofo);
        if (data.length() == 6){
        if(data.charAt(0)=='1'){
           sl1.write(30);
           digitalWrite(led0,HIGH);
        }else{
        digitalWrite(led0,LOW);
        sl1.write(0);
        }
        if(data.charAt(1)=='1'){
            digitalWrite(led1,HIGH);
            sl2.write(20);
        }else{
        digitalWrite(led1,LOW);
        sl2.write(0);
        }
         if(data.charAt(2)=='1'){
             digitalWrite(led2,HIGH);
             sl3.write(20);
        }else{
        digitalWrite(led2,LOW);
        sl3.write(0);
        }
         if(data.charAt(3)=='1'){
             digitalWrite(led3,HIGH);
             sr1.write(20);
        }else{
        digitalWrite(led3,LOW);
        sr1.write(0);
        }
         if(data.charAt(4)=='1'){
         digitalWrite(led4,HIGH);
         sr2.write(20);
        }else{
        digitalWrite(led4,LOW);
        sr2.write(0);
        }
         if(data.charAt(5)=='1'){
         digitalWrite(led5,HIGH);
         sr3.write(160);
        }else{
        digitalWrite(led5,LOW);
        sr3.write(100);
        }
        //get directions
        if(data == "llllll"){
            sl1.write(30);
            sl2.write(20);
            sl3.write(20);
            digitalWrite(led0,HIGH);
            digitalWrite(led1,HIGH);
            digitalWrite(led2,HIGH);
            delay(1000);
            sl1.write(0);
            sl2.write(0);
            sl3.write(0);
            digitalWrite(led0,LOW);
            digitalWrite(led1,LOW);
            digitalWrite(led2,LOW);
        }else if(data == "rrrrrr"){
            sr1.write(20);
            sr2.write(20);
            sr3.write(160);
            digitalWrite(led3,HIGH);
            digitalWrite(led4,HIGH);
            digitalWrite(led5,HIGH);
            delay(1000);
            sr1.write(0);
            sr2.write(0);
            sr3.write(100);
            digitalWrite(led3,LOW);
            digitalWrite(led4,LOW);
            digitalWrite(led5,LOW);
        }else if(data == "ffffff" || data == "ssssss" ){
            sl1.write(30);
            sr1.write(20);
            digitalWrite(led0,HIGH);
            digitalWrite(led3,HIGH);
            delay(1000);
            sl1.write(0);
            sr1.write(0);
            digitalWrite(led0,LOW);
            digitalWrite(led3,LOW);
        }else if(data == "bbbbbb"){
            sl3.write(20);
            sr3.write(160);
            digitalWrite(led2,HIGH);
            digitalWrite(led5,HIGH);
            delay(1000);
            sl3.write(0);
            sr3.write(100);
            digitalWrite(led2,LOW);
            digitalWrite(led5,LOW);
        }
    
    }
}
delay(250); //1 second delay after each letter
}
