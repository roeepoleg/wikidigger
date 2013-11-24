var events = require("events");
var http = require("http");
var util = require("util");

function ArticleFetcher() {
  events.EventEmitter.call(this);
  var self = this;

  /*
  * fetch article data from the web. parse it into an object
  * Dispatch onArticleLoaded for success and onErrorParsingArticle for fail.
  */
  this.getArticle = function(subject){
    console.log("getArticle:"+subject);
    var url = "http://en.wikipedia.org/w/api.php?format=json&action=query&prop=revisions&rvprop=content&titles=";

    http.get(url+subject, function(res) {

      var jsonStr = "";
      res.setEncoding('utf8');
      // getting data
      res.on('data', function (chunk) {

        jsonStr += chunk;//JSON.stringify(chunk);
      });

      res.on('end', function(){
          // extract only [[title]] from the whole text
          var resArray = jsonStr.match(/\[\[([^\]\],^\:]*)\]\]/g);
        if (resArray){
          var errorFound = false;
          var nextTitleStr = "";
          do {
            try{
              // select random article value
              var index = Math.floor(Math.random()*(resArray.length-1));
              // clean it from [[]] and | that might be there
              nextTitleStr = resArray[index];
              //console.log(nextTitleStr);
              var noBracketRegex = /\[\[(.*)\]\]/;
              nextTitleStr = noBracketRegex.exec(nextTitleStr)[1];

              if (nextTitleStr.indexOf('|') >= 0){
                nextTitleStr = nextTitleStr.substr(0,nextTitleStr.indexOf('|'));
              }
              nextTitleStr = nextTitleStr.replace(/\s/g,'_');
              //console.log(nextTitleStr);
              
            }catch (error){
              errorFound = true;
              console.log("ERROR -----> "+ error);
            }
          } while (errorFound)
          
          var resObj =  {"title":subject,
                  "url":"http://en.wikipedia.org/wiki/"+subject,
                  "nextTitle":nextTitleStr};
                   //console.log(resObj);
          self.emit('onArticleLoaded',resObj);
        }
        else {
          console.log("ERROR -----> no links for "+ subject);
          self.emit('onErrorParsingArticle',subject);
        }
      });

      res.on('error', function(err){
        console.log("ERROR -----> "+ err);
        self.emit('onErrorParsingArticle',subject);
      });
    });
  }
}
util.inherits(ArticleFetcher, events.EventEmitter);

function WikiParser() {
  events.EventEmitter.call(this);

  var self = this;
  var refHash = {};
  var articles = [];
  var articleFetcher = new ArticleFetcher();

  this.populateQuizCollection = function(initialArticle, stackSize){

    stackSize = stackSize || 100;
    
    articleFetcher.on('onArticleLoaded', function(article){
      console.log('articles loaded: '+articles.length);
      errorCounter = 0;
      articles.push(article);
      refHash[article.title] = article;
      lastAddedArticle = article;

      if (articles.length < stackSize)
        articleFetcher.getArticle(article.nextTitle);
      else{
        self.emit('onAllArticlesLoaded',articles.length);
      }
    });

    articleFetcher.on('onErrorParsingArticle', function(articleTitle){
      errorCounter++;
      console.log('onErrorParsingArticle:   '+articleTitle);
      articles.pop();
      delete refHash[lastAddedArticle.title];
      // pop twice in a row
      if (errorCounter > 3){
        lastAddedArticle = articles.pop();
        delete refHash[lastAddedArticle.title];
      }
      
      articleFetcher.getArticle(lastAddedArticle.title);
    });

    articleFetcher.getArticle(initialArticle);
  }

  this.getQuiz = function (title){

    // if (articles && refHash)
    var quiz = {};
    var startArticle = articles[0];
    var startIndex = 0;

    if (title){
      startArticle = refHash[title];
      startIndex = articles.indexOf(startArticle);
      // make sure we dont step out of bounds
      if (startIndex+6 >= articles.length){
        startArticle = articles[0];
        startIndex = 0;
      }
      
    }

    quiz.question = startArticle;
    quiz.answers = [];
    var usedTitles = {};

    for (var i = 2; quiz.answers.length < 4; i++) {
      var article = articles[i+startIndex];
      // prevent duplicates
      if (!usedTitles[article.title]){
      	  usedTitles[article.title] = true;
	      article.isCorrect = i == 2;
	      article.distance = i;
	      quiz.answers.push(article);
      }
// console.log(quiz.answers.length);
    }

    return quiz;
  }
}
util.inherits(WikiParser, events.EventEmitter);


exports.ArticleFetcher = ArticleFetcher;
exports.WikiParser = WikiParser;