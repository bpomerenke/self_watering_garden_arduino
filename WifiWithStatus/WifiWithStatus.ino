/****************************************************************
Mostly copied / modified from WebClient.ino
Shawn Hymel @ SparkFun Electronics
March 1, 2014
https://github.com/sparkfun/SFE_CC3000_Library

Manually connects to a WiFi network and performs an HTTP GET
request on a web page. Prints the contents of the page to
the serial console.

The security mode is defined by one of the following:
WLAN_SEC_UNSEC, WLAN_SEC_WEP, WLAN_SEC_WPA, WLAN_SEC_WPA2

Hardware Connections:
 
 Uno Pin    CC3000 Board    Function
 
 +5V        VCC or +5V      5V
 GND        GND             GND
 2          INT             Interrupt
 7          EN              WiFi Enable
 10         CS              SPI Chip Select
 11         MOSI            SPI MOSI
 12         MISO            SPI MISO
 13         SCK             SPI Clock
****************************************************************/

#include <SPI.h>
#include <SFE_CC3000.h>
#include <SFE_CC3000_Client.h>
#include <OneWire.h>

// Pins
#define CC3000_INT      2   // Needs to be an interrupt pin (D2/D3)
#define CC3000_EN       7   // Can be any digital pin
#define CC3000_CS       10  // Preferred is pin 10 on Uno
#define voltageFlipPin1 6
#define voltageFlipPin2 8
#define sensorPin 0
#define pumpPin 3

int DS18S20_Pin = 4; //DS18S20 Signal pins
//Temperature chip i/o
OneWire ds(DS18S20_Pin);

int flipTimer = 1000;


// Connection info data lengths
#define IP_ADDR_LEN     4   // Length of IP address in bytes

// Constants
char ap_ssid[] = "GALAXY_S4_3387";        // SSID of network
char ap_password[] = "qovq0568";          // Password of network
unsigned int ap_security = WLAN_SEC_WPA2; // Security of network
unsigned int timeout = 30000;             // Milliseconds
char server[] = "www.bpomerenke.com";        // Remote host site

// Global Variables
SFE_CC3000 wifi = SFE_CC3000(CC3000_INT, CC3000_EN, CC3000_CS);
SFE_CC3000_Client client = SFE_CC3000_Client(wifi);

void setup() {
  
  ConnectionInfo connection_info;
  int i;
  
  // Initialize Serial port
  Serial.begin(115200);
  Serial.println();
  Serial.println("---------------------------");
  Serial.println("Self Watering Garden");
  Serial.println("---------------------------");
  
  pinMode(voltageFlipPin1, OUTPUT);
  pinMode(voltageFlipPin2, OUTPUT);
  pinMode(sensorPin, INPUT);
  pinMode(pumpPin, OUTPUT);
  
  // Initialize CC3000 (configure SPI communications)
  if ( wifi.init() ) {
    Serial.print(".");
  } else {
    Serial.println("Something went wrong during CC3000 init!");
  }
}
void wifiInit()
{ 
  // Initialize CC3000 (configure SPI communications)
  if ( wifi.init() ) {
    Serial.print(".");
  } else {
    Serial.println("Something went wrong during CC3000 init!");
  }
}
void makeRequest(String url)
{
  ConnectionInfo connection_info;
  int i;

  // Connect using DHCP
  Serial.print(".");
  //Serial.println(ap_ssid);
  if(!wifi.connect(ap_ssid, ap_security, ap_password, timeout)) {
    Serial.println("Error: Could not connect to AP");
  }
  
  // Gather connection details and print IP address
  if ( !wifi.getConnectionInfo(connection_info) ) {
    Serial.println("Error: Could not obtain connection details");
  } else {
    Serial.print(".");
    for (i = 0; i < IP_ADDR_LEN; i++) {
      //Serial.print(connection_info.ip_address[i]);
      if ( i < IP_ADDR_LEN - 1 ) {
        //Serial.print(".");
      }
    }
    //Serial.println();
  }
  
  // Make a TCP connection to remote host
  Serial.print(".");
  //Serial.println(server);
  if ( !client.connect(server, 80) ) {
    Serial.println("Error: Could not make a TCP connection");
  }
  
  // Make a HTTP GET request
  client.println("GET "+url+" HTTP/1.1");
  client.print("Host: ");
  client.println(server);
  client.println("Connection: close");
  client.println();
  Serial.println("Hitting url: " + url); 
}

String getResponse()
{
  String result = "";
  boolean keepReading = true;
  
  while(keepReading)
  {
    // If there are incoming bytes, print them
    if ( client.available() ) {
      char c = client.read();
      result += c;
    }
    
    // If the server has disconnected, stop the client and wifi
    if ( !client.connected() ) {
      Serial.println();
      
      // Close socket
      if ( !client.close() ) {
        Serial.println("Error: Could not close socket");
      }
      
      // Disconnect WiFi
      if ( !wifi.disconnect() ) {
        Serial.println("Error: Could not disconnect from network");
      }
      
      // Do nothing
      //Serial.println("Finished WebClient test");
      keepReading = false;
    }
    else
    {
       keepReading = true; 
    }
  } 
  return result;
}

String getStatus()
{  
  wifiInit();
  makeRequest("/garden/getCommand.php");
  return getResponse();
}
String postSensorData(float temp, float moisture)
{  
  wifiInit();
  char tempStr[10];
  char moistureStr[10];
  
  dtostrf(temp, 1,2,tempStr);
  dtostrf(moisture,1,2,moistureStr);
  
  String url = "/garden/updateStats.php?temp=";
  url += tempStr;
  url += "&moisture=";
  url += moistureStr;
  makeRequest(url);
  return getResponse();
}

void timedDelay(int sec, boolean showCount)
{
   if(showCount)
   {
     String message = "Delaying ";
     message+=sec;
     message+=" sec...";
      Serial.print(message); 
   }
   for(int i = sec; i>=0; i--)
   {
     delay(1000);
     if(showCount)
     {
       String message = "..";
       message += i;
       Serial.print(message);
     }
   }
   Serial.println("");
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

/*
  if ( OneWire::crc8( addr, 7) != addr[7]) {
      Serial.println("CRC is not valid!");
      return -1000;
  }
*/
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

void setSensorPolarity(boolean flip){
  if(flip){
    digitalWrite(voltageFlipPin1, HIGH);
    digitalWrite(voltageFlipPin2, LOW);
  }else{
    digitalWrite(voltageFlipPin1, LOW);
    digitalWrite(voltageFlipPin2, HIGH);
  }
}

float getMoisture(){
  setSensorPolarity(true);
  delay(flipTimer);
  int val1 = analogRead(sensorPin);
  delay(flipTimer);  
  setSensorPolarity(false);
  delay(flipTimer);
  // invert the reading
  int val2 = 1023 - analogRead(sensorPin);
  //
  return (val1 + val2) / 2.0; 
}

void loop() {
  float moisture = getMoisture();
  float temperature = getTemp();
  float farenheight = (temperature * 9.0 / 5.0) + 32;
  
  Serial.print("temperature:");
  Serial.println(farenheight);
  Serial.print("moisture:");
  Serial.println(moisture);
  
  String statusVal = getStatus();
  Serial.println(statusVal);
  Serial.println(postSensorData(farenheight,moisture));
  if(statusVal.endsWith("water") || moisture < 100.0)
  {
    Serial.println("");
    Serial.println("watering the garden now....");
    digitalWrite(pumpPin,HIGH);
    timedDelay(60, true);
    digitalWrite(pumpPin, LOW);
  }
  else
  {
    digitalWrite(pumpPin,LOW);
    Serial.print("-"); 
  }
  
  timedDelay(5, true);
  
}

