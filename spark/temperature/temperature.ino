#include "DS18B20.h"
#include "OneWire.h"
#define voltageFlipPin1 6
#define voltageFlipPin2 7
#define sensorPin 0

DS18B20 ds18b20 = DS18B20(D2);
char szInfo[64];
double temp = 0.0;

unsigned long lastloop = 0;
int flipTimer = 500;

void setup() {
  Serial1.begin(9600);
  pinMode(voltageFlipPin1, OUTPUT);
  pinMode(voltageFlipPin2, OUTPUT);
  pinMode(sensorPin, INPUT);

  Spark.variable("tempstring", szInfo, STRING);
  Spark.variable("temp", &temp, DOUBLE);

  lastloop = millis();
}

void loop() {
  if ((millis() - lastloop) < 15000) return;

  if(!ds18b20.search()){
    Serial1.println("No more addresses.");
    Serial1.println();
    ds18b20.resetsearch();
    delay(250);

    return;
  }

  float celsius = ds18b20.getTemperature();
  float fahrenheit = ds18b20.convertToFahrenheit(celsius);

  sprintf(szInfo, "Temperature: %2.2f Celsius, %2.2f Fahrenheit (Chip Name: %s)", celsius, fahrenheit, ds18b20.getChipName());

  temp = (double)(fahrenheit);
  Spark.publish("temp", (String)temp, 60, PRIVATE);

  int moisture = getMoisture();
  Spark.publish("moisture", (String)moisture, 60, PRIVATE);

  lastloop = millis();
}

int getMoisture() {
  setSensorPolarity(true);
  delay(flipTimer);
  int val1 = analogRead(sensorPin);
  delay(flipTimer);
  setSensorPolarity(false);
  delay(flipTimer);
  // invert the reading
  int val2 = 4095 - analogRead(sensorPin);
  int avg = (val1 + val2) / 2;

  return avg;
}

void setSensorPolarity(boolean flip){
  if(flip){
    digitalWrite(voltageFlipPin1, HIGH);
    digitalWrite(voltageFlipPin2, LOW);
  }else{
    digitalWrite(voltageFlipPin1, LOW);
    digitalWrite(voltageFlipPin2, HIGH);
  }
}
