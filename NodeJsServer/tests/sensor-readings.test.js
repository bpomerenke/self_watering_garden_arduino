var superagent = require('superagent')
var expect = require('expect.js')

describe('Manage Sensor Readings', function(){
  var urlPrefix = 'http://localhost:3000/api/v1/'
  var id

  it('post object', function(done){
    superagent.post(urlPrefix + 'sensor/MySensorId/sensorReading')
      .send(
		{ temp: 98.6, moisture: 400, takenAt: new Date('2014-01-02T14:56:59.301Z') }
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

  it('retrieves an object', function(done){
    superagent.get(urlPrefix + 'sensor/MySensorId/sensorReading/' +id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)
        expect(res.body._id).to.eql(id)
		expect(res.body.temp).to.eql(98.6)
		expect(res.body.moisture).to.eql(400)
		expect(res.body.takenAt).to.eql('2014-01-02T14:56:59.301Z')
        done()
      })
  })
  
  it('retrieves a collection', function(done){
    superagent.get(urlPrefix + 'sensor/MySensorId/sensorReading')
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(res.body.length).to.be.above(0)
        expect(res.body.map(function (item){return item._id})).to.contain(id)        
        done()
      })
  })

  it('updates an object', function(done){
    superagent.put(urlPrefix + 'sensor/MySensorId/sensorReading/' + id)
      .send(
		{ temp: 22, moisture: 100, takenAt: new Date('2014-01-01T14:56:59.301Z') }
      )
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body.msg).to.eql('success')  
        done()
      })
  })
  
  it('checks an updated object', function(done){
    superagent.get(urlPrefix + 'sensor/MySensorId/sensorReading/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)        
        expect(res.body._id).to.eql(id)        
		expect(res.body.temp).to.eql(22)
		expect(res.body.moisture).to.eql(100)
		expect(res.body.takenAt).to.eql('2014-01-01T14:56:59.301Z')
        done()
      })
  })    

 it('removes an object', function(done){
    superagent.del(urlPrefix + 'sensor/MySensorId/sensorReading/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body.msg).to.eql('success')    
        done()
      })
  }) 
  
  it('Verify reading deleted', function(done){
    superagent.get(urlPrefix + 'sensor/MySensorId/sensorReading/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
		expect(res.body).to.be.empty()
        done()
      })
  })    
})