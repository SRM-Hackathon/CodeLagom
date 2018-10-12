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
}