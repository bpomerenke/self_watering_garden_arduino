This folder contains a simple node.js server that communicates with a mongoDB instance.

To get it running:
- Install node.js
- Install mongoDB
- type 'npm install' to download all third party node packages
- start your mondo DB
- type 'node server.js' to start the server

To run tests
- Install Mocha test running framework gobally via 'npm install -g mocha'
- CD into tests directory
- run 'mocha *'

Domain objects:

Sensor
 - _id
 - name
 - wateringSchedule 
 -- startWhenBelowMoisture
 -- stopWhenAboveMoisture

SensorReading
 - _id
 - sensor sensor(_id)
 - takenAt (date-time)
 - temp
 - moisture

User
 - _id
 - username
 - sensors [sensor(_id)]