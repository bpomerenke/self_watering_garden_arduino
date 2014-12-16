#include <OneWire.h>
#define voltageFlipPin1 6
#define voltageFlipPin2 7
#define sensorPin 0

int DS18S20_Pin = 2; //DS18S20 Signal pin on digital 2
int flipTimer = 1000;

//Temperature chip i/o
OneWire ds(DS18S20_Pin);  // on digital pin 2

void setup(){
  Serial.begin(9600);
  pinMode(voltageFlipPin1, OUTPUT);
  pinMode(voltageFlipPin2, OUTPUT);
  pinMode(sensorPin, INPUT);
       
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


void loop(){
  
  //
  setSensorPolarity(true);
  delay(flipTimer);
  int val1 = analogRead(sensorPin);
  delay(flipTimer);  
  setSensorPolarity(false);
  delay(flipTimer);
  // invert the reading
  int val2 = 1023 - analogRead(sensorPin);
  //
  reportLevels(val1,val2);
  float temperature = getTemp();
  float farenheight = (temperature * 9.0 / 5.0) + 32;
  Serial.println(farenheight);
    
}

float getTemp(){
  //returns the temperature from one DS18S20 in DEG Celsius

  byte data[12];
  byte addr[8];

  if ( !ds.search(addr)) {
      //no more sensors on chain, reset search
      ds.reset_search();
      return -1000;
  }

  if ( OneWire::crc8( addr, 7) != addr[7]) {
      Serial.println("CRC is not valid!");
      return -1000;
  }

  if ( addr[0] != 0x10 && addr[0] != 0x28) {
      Serial.print("Device is not recognized");
      return -1000;
  }

  ds.reset();
  ds.select(addr);
  ds.write(0x44,1); // start conversion, with parasite power on at the end

  byte present = ds.reset();
  ds.select(addr);    
  ds.write(0xBE); // Read Scratchpad

  
  for (int i = 0; i < 9; i++) { // we need 9 bytes
    data[i] = ds.read();
  }
  
  ds.reset_search();
  
  byte MSB = data[1];
  byte LSB = data[0];

  float tempRead = ((MSB << 8) | LSB); //using two's compliment
  float TemperatureSum = tempRead / 16;
  
  return TemperatureSum;
  
}




void reportLevels(int val1,int val2){
  
  int avg = (val1 + val2) / 2;
  
  String msg = "avg: ";
  msg += avg;
  Serial.println(msg);

}

