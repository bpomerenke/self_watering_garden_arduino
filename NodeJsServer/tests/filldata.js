var superagent = require('superagent')
var expect = require('expect.js')
	
describe('Add Data For Manual Testing', function(){
  var urlPrefix = 'http://localhost:3000/api/v1/'
  var id
  
  it('Post Sensor', function(done){
    superagent.post(urlPrefix + 'sensor')
      .send(
		{ name: 'Sensor', wateringSchedule : { startWhenBelowMoisture : 300, stopWhenAboveMositure : 400 } }
      )
      .end(function(e,res){
		// console.log(res.body)
        expect(e).to.eql(null)
        expect(res.body.length).to.eql(1)
        expect(res.body[0]._id.length).to.eql(24)
        id = res.body[0]._id
        done()
      })
  })
  
  it('Post SensorReading', function(done){
    superagent.post(urlPrefix + 'sensor/' + id + '/sensorReading')
      .send(
		{ sensorId: id, temp: 98.6, moisture: 400 }
      )
      .end(function(e,res){
		// console.log(res.body)
        expect(e).to.eql(null)
        expect(res.body.length).to.eql(1)
        expect(res.body[0]._id.length).to.eql(24)
        id = res.body[0]._id
        done()
      })
  })
    
})