var express = require('express')
  , mongoskin = require('mongoskin')
  , bodyParser = require('body-parser')

var app = express()
app.use(bodyParser())

var db = mongoskin.db('mongodb://@localhost:27017/mydb', {safe:true})

app.param('collectionName', function(req, res, next, collectionName){
  req.collection = db.collection(collectionName)
  return next()
})

app.get('/', function(req, res, next) {
  res.send('please select an endpoint, e.g., /api/v1/sensorReading')
})

app.get('/api/v1/:collectionName', function(req, res, next) {
  req.collection.find({} ,{limit:10, sort: [['_id',-1]]}).toArray(function(e, results){
    if (e) return next(e)
    res.send(results)
  })
})

app.post('/api/v1/:collectionName', function(req, res, next) {
  req.collection.insert(req.body, {}, function(e, results){
    if (e) return next(e)
    res.send(results)
  })
})

app.get('/api/v1/:collectionName/:id', function(req, res, next) {
  req.collection.findById(req.params.id, function(e, result){
    if (e) return next(e)
    res.send(result)
  })
})

app.put('/api/v1/:collectionName/:id', function(req, res, next) {
  req.collection.updateById(req.params.id, {$set:req.body}, {safe:true, multi:false}, function(e, result){
    if (e) return next(e)
    res.send((result===1)?{msg:'success'}:{msg:'error'})
  })
})

app.delete('/api/v1/:collectionName/:id', function(req, res, next) {
  req.collection.removeById(req.params.id, function(e, result){
    if (e) return next(e)
    res.send((result===1)?{msg:'success'}:{msg:'error'})
  })
})

app.post('/api/v1/sensor/:sensorId/sensorReading', function(req, res, next) {
  sensorReadingCollection = db.collection('sensor_' + req.params.sensorId + '_sensorReading')
  sensorReadingCollection.insert(req.body, {}, function(e, results){
    if (e) return next(e)
    res.send(results)
  })
})

app.get('/api/v1/sensor/:sensorId/sensorReading', function(req, res, next) {
  sensorReadingCollection = db.collection('sensor_' + req.params.sensorId + '_sensorReading')
  var limit = req.query.limit
  sensorReadingCollection.find({} ,{limit:limit, sort: [['_id',-1]]}).toArray(function(e, results){
    if (e) return next(e)
    res.send(results)
  })
})

app.get('/api/v1/sensor/:sensorId/sensorReading/:id', function(req, res, next) {
  sensorReadingCollection = db.collection('sensor_' + req.params.sensorId + '_sensorReading')
  sensorReadingCollection.findById(req.params.id, function(e, result){
    if (e) return next(e)
    res.send(result)
  })
})

app.put('/api/v1/sensor/:sensorId/sensorReading/:id', function(req, res, next) {
  sensorReadingCollection = db.collection('sensor_' + req.params.sensorId + '_sensorReading')
  sensorReadingCollection.updateById(req.params.id, {$set:req.body}, {safe:true, multi:false}, function(e, result){
    if (e) return next(e)
    res.send((result===1)?{msg:'success'}:{msg:'error'})
  })
})

app.delete('/api/v1/sensor/:sensorId/sensorReading/:id', function(req, res, next) {
  sensorReadingCollection = db.collection('sensor_' + req.params.sensorId + '_sensorReading')
  sensorReadingCollection.removeById(req.params.id, function(e, result){
    if (e) return next(e)
    res.send((result===1)?{msg:'success'}:{msg:'error'})
  })
})

app.post('/api/v1/user/authenticate', function(req, res, next) {
  userCollection = db.collection('user')
  userCollection.findOne({username: req.body.username}, function(e, result){
    if (e) return next(e)
	if(result) {
		res.send({ token :'TODO' })
	} else {
	   res.status(401).send({ error : 'Invalid Login'} )
	}
  })
})

app.listen(3000)