#include <SPI.h>
#include <SFE_CC3000.h>
#include <SFE_CC3000_Client.h>
#include<stdlib.h>

/*******************************************************
 * Edit app_ssid & ap_password with your wifi ssid and password.
 * Point browser at http://onedogdev.com/results.php
 * When script sends data it should show up there.
 *******************************************************/

// Pins
#define CC3000_INT      2   // Needs to be an interrupt pin (D2/D3)
#define CC3000_EN       7   // Can be any digital pin
#define CC3000_CS       10  // Preferred is pin 10 on Uno

// Connection info data lengths
#define IP_ADDR_LEN     4   // Length of IP address in bytes

// Constants
char ap_ssid[] = "ljmobile";                  // SSID of network
char ap_password[] = "password";          // Password of network
char server[] = "onedogdev.com";        // Remote host site
unsigned int ap_security = WLAN_SEC_WPA2; // Security of network
unsigned int timeout = 30000;             // Milliseconds
unsigned int collectionDelay = 5000; // The amount of time to sleep between reading collection and post

// Global Variables
SFE_CC3000 wifi = SFE_CC3000(CC3000_INT, CC3000_EN, CC3000_CS);
SFE_CC3000_Client client = SFE_CC3000_Client(wifi);

void setup() {
  // Initialize Serial port
  Serial.begin(115200);
  Serial.println();
  Serial.println("-------------------------------");
  Serial.println("Asynchrony Self Watering Garden");
  Serial.println("-------------------------------");
  
  randomSeed(analogRead(0)); // Read analog noise to seed randomizer
}

void loop() {
  Serial.println("===================== Next Attempt =============");
  if(initializeWifi()) {
    if(connectToAccessPoint()) {
      Serial.println("Collecting sensor data");
      // TODO get data from sensors
      float temp = random(100);
      int moisture = random(300);
      Serial.println("Sensor data collection complete");
      
      postValues(temp, moisture);
    }
  }
  Serial.println("Waiting before next loop");
  delay(collectionDelay);
}

boolean initializeWifi() {
  if(!wifi.getInitStatus()) {
    if ( wifi.init() ) {
      Serial.println("WIFI initialization complete");
    } else {
      Serial.println("Something went wrong during WIFI init!");
      return false;
    }
  } else {
    Serial.println("WIFI already initialized and ready to go");
  }
  return true;
}

boolean connectToAccessPoint() {
  if(!wifi.getConnectionStatus()) {
    // Connect to access point using DHCP
    Serial.print("Connecting to SSID: ");
    Serial.println(ap_ssid);
    if(!wifi.connect(ap_ssid, ap_security, ap_password, timeout)) {
      Serial.println("Error: Could not connect to AP");
      return false;
    }
    
    ConnectionInfo connection_info;
    int i;
    // Gather connection details and print IP address
    if ( !wifi.getConnectionInfo(connection_info) ) {
      Serial.println("Error: Could not obtain connection details");
      return false;
    } else {
      Serial.print("IP Address: ");
      for (i = 0; i < IP_ADDR_LEN; i++) {
        Serial.print(connection_info.ip_address[i]);
        if ( i < IP_ADDR_LEN - 1 ) {
          Serial.print(".");
        }
      }
      Serial.println();
    }
  } else {
      Serial.println("Already connected to AP");    
  }
  return true;
}

void postValues(float temp, int moisture) {
  client.flush();

  Serial.println("Connecting to server: ");
  Serial.println(server);
  if ( !client.connect(server, 80) ) {
    Serial.print("Error: Could not make a TCP connection to ");
    Serial.println(server);
    return;
  } else {
    Serial.print("Successfully connected to ");
    Serial.println(server);
  }
  
  char tempStr[10];
  dtostrf(temp, 1, 2, tempStr);

  String request = "/garden.php?temp=";
  request += tempStr;
  request += "&moisture=";
  request += moisture;
  request += " HTTP/1.1";
  
  Serial.println("Performing HTTP GET of: " + request);
  client.println("GET " + request + " HTTP/1.1");
  client.print("Host: ");
  client.println(server);
  client.println("Connection: close");
  client.println();
  
  client.flush();
  Serial.println("Completed GET.");
}
