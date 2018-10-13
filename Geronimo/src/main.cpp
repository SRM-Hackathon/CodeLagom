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
        if(data=="emohaa"){
            sl1.write(30);
            delay(1000);
            sl1.write(0);
        }else if(data=="emosad"){
            sl2.write(20);
            delay(1000);
            sl2.write(0);
        }else if(data=="emoang"){
            sl3.write(20);
            delay(1000);
            sl3.write(0);
        }else{}
    }
}
delay(250); //1 second delay after each letter
}
