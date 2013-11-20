var express = require("express");
var events = require('events');
 
/////
var eventEmitter = new events.EventEmitter();
var wikiparser = require("./wikiparser");
var app = express();

wikiparser.populate(eventEmitter);

app.use(express.logger());

app.get('/quiz', function(request, response) {
  response.send(wikiparser.getQuiz(null));
});
app.get('/quiz/:title', function(request, response) {
  response.send(wikiparser.getQuiz(request.params["title"]));
});

var port = process.env.PORT || 5000;

eventEmitter.on("onAllArticlesLoaded", function(){
    console.log("onAllArticlesLoaded!!! "+global.articles.length);

    app.listen(port, function() {
	  console.log("Listening on " + port);
	});
});	