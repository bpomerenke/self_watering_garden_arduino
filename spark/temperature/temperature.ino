#include "DS18B20.h"
#include "OneWire.h"

DS18B20 ds18b20 = DS18B20(D2);
char szInfo[64];
double temp = 0.0;

unsigned long lastloop = 0;

void setup() {
  Serial1.begin(9600);
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

  lastloop = millis();
  delay(1000);  // delay just in case it gets run more often
}
