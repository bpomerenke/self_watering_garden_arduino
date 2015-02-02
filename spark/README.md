# Spark IO Core Code

## To get code working:
1. provision a core with wifi credentials and claim it (use tinker app or follow usb/terminal instructions)
2. Use spark web IDE, and manually create the files for the project you're interested in
3. Copy/paste code from github for each file
4. flash core with code
5. Note your device ID and auth token


## For Temperature project:
* curl https://api.spark.io/v1/devices/{{DEVICE ID HERE}}/temp?access_token={{ACCESS TOKEN HERE}}
  * exposed variable that just gives temp in farhenheit (NOTE: variable is a cast from float to double)

* curl https://api.spark.io/v1/devices/{{DEVICE ID HERE}}/temp_string?access_token={{ACCESS TOKEN HERE}}
  * exposed variable that is a string that show temp in farhenheit and celsius.

