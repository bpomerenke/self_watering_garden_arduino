var superagent = require('superagent')
var expect = require('expect.js')
	
describe('Manage Users', function(){
  var urlPrefix = 'http://localhost:3000/api/v1/'
  var id
  
  it('post object', function(done){
    superagent.post(urlPrefix + 'user')
      .send(
		{ username: 'UserName', sensors : [ { sensorId : 'SensorId1' }, { sensorId : 'SensorId2' } ] }
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
    superagent.get(urlPrefix + 'user/' +id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)
        expect(res.body._id).to.eql(id)
		expect(res.body.username).to.eql('UserName')
		expect(res.body.sensors[0].sensorId).to.eql('SensorId1')
		expect(res.body.sensors[1].sensorId).to.eql('SensorId2')
        done()
      })
  })

  it('retrieves a collection', function(done){
    superagent.get(urlPrefix + 'user')
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(res.body.length).to.be.above(0)
        expect(res.body.map(function (item){return item._id})).to.contain(id)        
        done()
      })
  })

  it('updates an object', function(done){
    superagent.put(urlPrefix + 'user/' + id)
      .send(
		{ username: 'NewUserName', sensors : [ { sensorId : 'NewSensorId1' }, { sensorId : 'NewSensorId2' } ] }
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
    superagent.get(urlPrefix + 'user/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)        
        expect(res.body._id).to.eql(id)        
		expect(res.body.username).to.eql('NewUserName')
		expect(res.body.sensors[0].sensorId).to.eql('NewSensorId1')
		expect(res.body.sensors[1].sensorId).to.eql('NewSensorId2')
        done()
      })
  })    
  
 it('removes an object', function(done){
    superagent.del(urlPrefix + 'user/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body.msg).to.eql('success')    
        done()
      })
  }) 
  
  it('Verify reading deleted', function(done){
    superagent.get(urlPrefix + 'user/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
		expect(res.body).to.be.empty()
        done()
      })
  })    
})