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

// Pins
#define CC3000_INT      2   // Needs to be an interrupt pin (D2/D3)
#define CC3000_EN       7   // Can be any digital pin
#define CC3000_CS       10  // Preferred is pin 10 on Uno

// Connection info data lengths
#define IP_ADDR_LEN     4   // Length of IP address in bytes

// Constants
char ap_ssid[] = "pomtower";                  // SSID of network
char ap_password[] = "hello123";          // Password of network
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
  Serial.println(); 
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
  makeRequest("/status.php");
  return getResponse();
}
String postSensorData(float temp, float moisture)
{  
  wifiInit();
  char tempStr[10];
  char moistureStr[10];
  
  dtostrf(temp, 1,2,tempStr);
  dtostrf(moisture,1,2,moistureStr);
  
  String url = "/garden.php?temp=";
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
void loop() {
  String statusVal = getStatus();
  float moisture = random(400);
  float temp = random(100);
  
  Serial.println(statusVal);
  Serial.println(postSensorData(temp,moisture));
  if(statusVal.endsWith("water\n"))
  {
    Serial.println("");
    Serial.println("watering the garden now....");
  }
  else
  {
     Serial.print("-"); 
  }
  
  timedDelay(15, true);
  
}

