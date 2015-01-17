#define SSID        "ljmobile"
#define PASS        "password"
#define TIMEOUT     20000 // mS
#define CONTINUE    false
#define HALT        true
#define ECHO_COMMANDS true
#define DST_IP      "74.220.207.196"

// Print error message and loop stop.
void errorHalt(String msg)
{
  Serial.println(msg);
  Serial.println("HALT");
  while(true){};
}

// Read characters from WiFi module and echo to serial until keyword occurs or timeout.
boolean echoFind(String keyword)
{
  byte current_char   = 0;
  byte keyword_length = keyword.length();
  
  // Fail if the target string has not been sent by deadline.
  long deadline = millis() + TIMEOUT;
  while(millis() < deadline)
  {
    if (Serial1.available())
    {
      char ch = Serial1.read();
      Serial.write(ch);
      if (ch == keyword[current_char])
        if (++current_char == keyword_length)
        {
          Serial.println();
          return true;
        }
    }
  }
  return false;  // Timed out
}

// Read and echo all available module output.
// (Used when we're indifferent to "OK" vs. "no change" responses or to get around firmware bugs.)
void echoFlush()
  {while(Serial1.available()) Serial.write(Serial1.read());}
  
// Echo module output until 3 newlines encountered.
// (Used when we're indifferent to "OK" vs. "no change" responses.)
void echoSkip()
{
  echoFind("\n");        // Search for nl at end of command echo
  echoFind("\n");        // Search for 2nd nl at end of response.
  echoFind("\n");        // Search for 3rd nl at end of blank line.
}

boolean echoCommand(String cmd, String ack, boolean halt_on_fail)
{
  Serial1.println(cmd);
  #ifdef ECHO_COMMANDS
    Serial.print("--"); Serial.println(cmd);
  #endif
  
  // If no ack response specified, skip all available module output.
  if (ack == "")
    echoSkip();
  else
    // Otherwise wait for ack.
    if (!echoFind(ack))          // timed out waiting for ack string 
      if (halt_on_fail)
        errorHalt(cmd+" failed");// Critical failure halt.
      else
        return false;            // Let the caller handle it.
  return true;                   // ack blank or ack found
}

// Connect to the specified wireless network.
boolean connectWiFi()
{
  String cmd = "AT+CWJAP=\""; cmd += SSID; cmd += "\",\""; cmd += PASS; cmd += "\"";
  if (echoCommand(cmd, "OK", HALT)) // Join Access Point
  {
    Serial.println("Connected to WiFi.");
    return true;
  }
  else
  {
    Serial.println("Connection to WiFi failed.");
    return false;
  }
}

void setup()  
{
  Serial.begin(115200);         // Communication with PC monitor via USB
  Serial1.begin(115200);        // Communication with ESP8266 via 5V/3.3V level shifter
//  Serial1.begin(57600);
//  Serial1.begin(9600);
    
  Serial1.setTimeout(TIMEOUT);
  Serial.println("ESP8266 Demo");
  
  delay(5000);

  echoCommand("AT+RST", "ready", HALT);    // Reset & test if the module is ready  
  Serial.println("Module is ready.");
  echoCommand("AT+GMR", "OK", CONTINUE);   // Retrieves the firmware ID (version number) of the module. 
//  echoCommand("AT+CWMODE?","OK", CONTINUE);// Get module access mode. 
  
  // echoCommand("AT+CWLAP", "OK", CONTINUE); // List available access points - DOESN't WORK FOR ME
  
  echoCommand("AT+CWMODE=1", "OK", HALT);    // Station mode
//  echoCommand("AT+CIPMUX=1", "", HALT);    // Allow multiple connections (we'll only use the first).

  //connect to the wifi
  boolean connection_established = false;
  for(int i=0;i<5;i++)
  {
    if(connectWiFi())
    {
      connection_established = true;
      break;
    }
  }
  if (!connection_established) errorHalt("Connection failed");
  
  delay(5000);
  
  String cmd = "AT+CIPSTART=\"TCP\",\"";  //make this command: AT+CPISTART="TCP","146.227.57.195",80
  cmd += DST_IP;
  cmd += "\",80";
  echoCommand(cmd, "Linked", HALT);
  
  cmd =  "GET /garden.php?temp=100&moisture=300 HTTP/1.0\r\n";  //construct http GET request
  cmd += "Host: onedogdev.com\r\n\r\n";        //test file on my web
  // + cmd.length()
  
  String atSend = "AT+CIPSEND=";
  atSend += cmd.length();
  echoCommand(atSend, ">", HALT);
  
  echoCommand(cmd, ">", CONTINUE);
//  Serial1.print("AT+CIPSEND=");                //www.cse.dmu.ac.uk/~sexton/test.txt
//  Serial1.println(cmd.length());

  Serial.print("-- waiting 30000");
  delay(30000);
  Serial.print("-- Done");
}


void loop() 
{
  delay(500);
}
