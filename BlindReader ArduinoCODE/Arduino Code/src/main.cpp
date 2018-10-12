#include <Arduino.h>
#include <Servo.h>

Servo s0,s1,s2,s3,s4,s5;
int led0=12, led1=9, led=13, led3=8, led4=11, led5=10;
String data="";
// Sending Code from Arduino to Android
byte val=0;
void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    s0.attach(7);
    s0.attach(6);
    s0.attach(5);
    s0.attach(4);
    s0.attach(3);
    s0.attach(2);
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
        val = analogRead(A0) 
        if(val > 5){
        Serial.println("k");
        delay(500);
    }
        else{
            char ch = Serial.read();
            if(ch == ',') {
                data+="";
            }
            else{
                data+=ch;
            }
            //Serial.println(data);
            if(data.length() ==6){
                if(data.charAt(0) == '1'){
                    s0.write(160);
                    digitalWrite(led0, HIGH);
                }
                else{
                    digitalWrite(led0, LOW)
                    s0.write(60)
                }
                if(data.charAt(1) == '1'){
                    digitalWrite(led1, HIGH)
                    s1.write(160)
                }else {
                    digitalWrite(led1, LOW)
                    s1.write(60)
                }
                if(data.charAt(2)== '1'){
                    digitalWrite(led2, HIGH)
                    s2.write(160)
                }else {
                    digitalWrite(led2, LOW)
                    s2.write(60)
                }if(data.charAt(3)== '1'){
                    digitalWrite(led3, HIGH)
                    s3.write(160)
                }else {
                    digitalWrite(led3, LOW)
                    s3.write(60)
                }if(data.charAt(4)== '1'){
                    digitalWrite(led4, HIGH)
                    s4.write(160)
                }else {
                    digitalWrite(led4, LOW)
                    s4.write(60)
                }if(data.charAt(5)== '1'){
                    digitalWrite(led5, HIGH)
                    s5.write(160)
                }else {
                    digitalWrite(led5, LOW)
                    s5.write(60)
                }
            }

    }}

}