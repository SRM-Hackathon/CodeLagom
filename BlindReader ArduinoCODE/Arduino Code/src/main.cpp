#include <Arduino.h>
#include <Servo.h>
#include <string.h>

Servo s0,s1,s2,s3,s4,s5;
int led0=12, led1=9, led2=13, led3=8, led4=11, led5=10;
String data="";
// Sending Code from Arduino to Android
byte val=0;
void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    s0.attach(7);
    s1.attach(6);
    s2.attach(5);
    s3.attach(4);
    s4.attach(3);
    s5.attach(2);
    pinMode(led0, OUTPUT);
    pinMode(led1, OUTPUT);
    pinMode(led2, OUTPUT);
    pinMode(led3, OUTPUT);
    pinMode(led4, OUTPUT);
    pinMode(led5, OUTPUT);
}

void loop() {
    // put your main code here, to run repeatedly:
    if(Serial.available()){
        val = analogRead(A0);
        if(val > 5){
        Serial.println("k");
        delay(500);
    }else{
            char ch = Serial.read();
            if(ch == ',') {
                data = "";
            }
            else{
                data+=ch;
            }
            //Serial.println(data);
            if (data.length() == 6){
                if(data.charAt(0)=='1'){
                    s0.write(160);
                    digitalWrite(led0,HIGH);
                }else{
                    digitalWrite(led0,LOW);
                    s0.write(60);
                }
                if(data.charAt(1)=='1'){
                    digitalWrite(led1,HIGH);
                    s1.write(160);
                }else{
                    digitalWrite(led1,LOW);
                    s1.write(60);
                }
                if(data.charAt(2)=='1'){
                    digitalWrite(led2,HIGH);
                    s2.write(160);
                }else{
                    digitalWrite(led2,LOW);
                    s2.write(60);
                }
                if(data.charAt(3)=='1'){
                    digitalWrite(led3,HIGH);
                    s3.write(60);
                }else{
                    digitalWrite(led3,LOW);
                    s3.write(160);
                }
                if(data.charAt(4)=='1'){
                    digitalWrite(led4,HIGH);
                    s4.write(60);
                }else{
                digitalWrite(led4,LOW);
                s4.write(160);
                }
                if(data.charAt(5)=='1'){
                    digitalWrite(led5,HIGH);
                    s5.write(60);
                }else{
                    digitalWrite(led5,LOW);
                    s5.write(160);
                }

    // Code for Emotion Representation
                if(data=="emohaa"){
                    s0.write(160);
                    delay(1000);
                    s0.write(60);
                }else if(data=="emosad"){
                    s1.write(160);
                    delay(1000);
                    s1.write(60);
                }else if(data=="emoang"){
                    s2.write(160);
                    delay(1000);
                    s2.write(60);
                }else if(data=="emofer"){
                    s3.write(60);
                    delay(1000);
                    s3.write(160);
                }else{}

    }
    }
}
delay(500); //1 second delay after each letter
}