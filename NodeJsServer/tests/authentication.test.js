var superagent = require('superagent')
var expect = require('expect.js')
	
describe('Authenticate', function(){
  var urlPrefix = 'http://localhost:3000/api/v1/'
  var id
  
  it('Invalid user', function(done){
    superagent.post(urlPrefix + 'user/authenticate')
      .send(
		{ username: 'BAD_USER_NAME' }
      )
      .end(function(e,res){
        expect(e).to.eql(null)
		expect(res.status).to.eql(401)
        expect(res.body.error).to.eql('Invalid Login')
        done()
      })
  })
  
  it('Valid user', function(done){
	superagent.post(urlPrefix + 'user')
      .send(
		{ username: 'ValidUser' }
      )
      .end(function(e,res){
        expect(e).to.eql(null)
		expect(res.status).to.eql(200)
    })
  
    superagent.post(urlPrefix + 'user/authenticate')
      .send(
		{ username: 'ValidUser' }
      )
      .end(function(e,res){
        expect(e).to.eql(null)
		expect(res.status).to.eql(200)
		//expect(res.body.token).to.be(undefined)
		expect(res.body.token).to.be.ok()
        done()
      })
  })
})