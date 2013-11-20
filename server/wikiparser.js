var http = require("http");

global.refHash = {};
global.articles = [];


/*
* Populate the articles array for future usage by API
*/
function populate (eventEmitter){

  var lastAddedArticle = null;

  eventEmitter.on('onArticleLoaded', function(article){
    errorCounter = 0;
    // console.log('onArticleLoaded:   '+article.title+", "+article.nextTitle);
    global.articles.push(article);
    global.refHash[article.title] = article;
    lastAddedArticle = article;

    console.log(global.articles.length);
    if (global.articles.length < 100)
      getArticle(article.nextTitle);
    else{
      eventEmitter.emit('onAllArticlesLoaded');
    }

  });
  //
  var errorCounter = 0;
  eventEmitter.on('onErrorParsingArticle', function(articleTitle){
    errorCounter++;
    console.log('onErrorParsingArticle:   '+articleTitle);
    global.articles.pop();
    delete global.refHash[lastAddedArticle.title];
    // pop twice in a row
    if (errorCounter > 3){
      lastAddedArticle = global.articles.pop();
      delete global.refHash[lastAddedArticle.title];
    }
    
    getArticle(lastAddedArticle.title);
  });
  // start with fixed title
  getArticle("Lance_Armstrong");
  
  /*
* Gets the data of given article 
*/
function getArticle (subject){
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
        var titleStr = "";
        do {
          try{
            // select random article value
            var index = Math.floor(Math.random()*(resArray.length-1));
            // clean it from [[]] and | that might be there
            titleStr = resArray[index];
            // console.log(titleStr);
            var noBracketRegex = /\[\[(.*)\]\]/;
            titleStr = noBracketRegex.exec(titleStr)[1];
            if (titleStr.indexOf('|') >= 0){
              titleStr = titleStr.substr(0,titleStr.indexOf('|'));
            }
            titleStr = titleStr.replace(/\s/g,'_');
            // console.log(titleStr);
            
          }catch (error){
            errorFound = true;
            console.log("ERROR -----> "+ error);
          }
        } while (errorFound)
        
        
        var resObj =  {"title":subject,
                "url":"http://en.wikipedia.org/wiki/"+subject,
                "nextTitle":titleStr};
        eventEmitter.emit('onArticleLoaded',resObj);
      }
      else {
        console.log("ERROR -----> no links for "+ subject);
        eventEmitter.emit('onErrorParsingArticle',subject);
      }
    });

    res.on('error', function(err){
      console.log("ERROR -----> "+ err);
    });
    });
}
}
/*
* Compose a question & 4 answers quiz
*/
function getQuiz (title){
  var quiz = {};
  var startArticle = global.articles[0];
  var startIndex = 0;

  if (title){
    startArticle = global.refHash[title];
    startIndex = global.articles.indexOf(startArticle);
    // make sure we dont step out of bounds
    if (startIndex+6 >= global.articles.length){
      startArticle = global.articles[0];
      startIndex = 0;
    }
    
  }

  quiz.question = startArticle;
  quiz.answers = [];
  
  for (var i = 2; i < 6; i++) {
    var article = global.articles[i+startIndex];
    article.isCorrect = i == 2;
    article.distance = i;
    quiz.answers.push(article);
  };

  return quiz;
}

exports.populate = populate;
exports.getQuiz = getQuiz;
