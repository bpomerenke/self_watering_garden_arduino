var superagent = require('superagent')
var expect = require('expect.js')
	
describe('Manage Sensors', function(){
  var urlPrefix = 'http://localhost:3000/api/v1/'
  var id
  
  it('post object', function(done){
    superagent.post(urlPrefix + 'sensor')
      .send(
		{ name: 'MySensorName', wateringSchedule : { startWhenBelowMoisture : 300, stopWhenAboveMositure : 400 } }
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
    superagent.get(urlPrefix + 'sensor/' +id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)
        expect(res.body._id).to.eql(id)
		expect(res.body.name).to.eql('MySensorName')
		expect(res.body.wateringSchedule.startWhenBelowMoisture).to.eql(300)
		expect(res.body.wateringSchedule.stopWhenAboveMositure).to.eql(400)
        done()
      })
  })

  it('retrieves a collection', function(done){
    superagent.get(urlPrefix + 'sensor')
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(res.body.length).to.be.above(0)
        expect(res.body.map(function (item){return item._id})).to.contain(id)        
        done()
      })
  })

  it('updates an object', function(done){
    superagent.put(urlPrefix + 'sensor/' + id)
      .send(
		{ name: 'NewSensorName', wateringSchedule : { startWhenBelowMoisture : 100, stopWhenAboveMositure : 150 } }
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
    superagent.get(urlPrefix + 'sensor/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body._id.length).to.eql(24)        
        expect(res.body._id).to.eql(id)        
		expect(res.body.name).to.eql('NewSensorName')
		expect(res.body.wateringSchedule.startWhenBelowMoisture).to.eql(100)
		expect(res.body.wateringSchedule.stopWhenAboveMositure).to.eql(150)
        done()
      })
  })    
  
 it('removes an object', function(done){
    superagent.del(urlPrefix + 'sensor/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
        expect(res.body.msg).to.eql('success')    
        done()
      })
  }) 
  
  it('Verify reading deleted', function(done){
    superagent.get(urlPrefix + 'sensor/' + id)
      .end(function(e, res){
        // console.log(res.body)
        expect(e).to.eql(null)
        expect(typeof res.body).to.eql('object')
		expect(res.body).to.be.empty()
        done()
      })
  })    
})