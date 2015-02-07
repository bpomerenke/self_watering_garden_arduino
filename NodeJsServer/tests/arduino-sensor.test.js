var superagent = require('superagent')
var expect = require('expect.js')

describe('Create Arduino Sensor', function(){
  var urlPrefix = 'http://104.236.196.37:3000/api/v1/'
  var id

  it('post object', function(done){
    superagent.post(urlPrefix + 'sensor')
      .send(
		{ name: 'Arduino', wateringSchedule : { startWhenBelowMoisture : 300, stopWhenAboveMositure : 400 } }
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