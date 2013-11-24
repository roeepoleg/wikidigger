var express = require("express");

var WikiParser = require('./wikiparser').WikiParser;
var wikiParser = new WikiParser();

var app = express();

app.use(express.logger());

app.get('/quiz', function(request, response) {
  response.send(wikiParser.getQuiz(null));
});
app.get('/quiz/:title', function(request, response) {
  response.send(wikiParser.getQuiz(request.params["title"]));
});

var port = process.env.PORT || 5000;

wikiParser.on("onAllArticlesLoaded", function(articlesCount){
    console.log("onAllArticlesLoaded!!! "+articlesCount);

    app.listen(port, function() {
	  console.log("Listening on " + port);
	});
});	
wikiParser.populateQuizCollection('Lance_Armstrong',100);