var equal = require('assert').equal,
	WikiParser = require('../wikiparser').WikiParser;
	ArticleFetcher = require('../wikiparser').ArticleFetcher;

exports.tests = {
	'Should return Lance_Armstrong article from wikipedia': function(finished, prefix) {	
		var articleFetcher = new ArticleFetcher();
		articleFetcher.on('onArticleLoaded', function(article){
		    equal('Lance_Armstrong', article.title, prefix+" Wrong title ");
		    finished();
		});
		articleFetcher.getArticle("Lance_Armstrong");

	},
	'Should return 5 articles': function(finished, prefix) {	
		var wikiParser = new WikiParser();
		wikiParser.on("onAllArticlesLoaded", function(articlesCount){
		    equal(5, articlesCount, prefix+" Wrong title ");
		    finished();		    
		});
		wikiParser.populateQuizCollection('Lance_Armstrong',5);
		
	},
	'Quiz Should not contain duplicate answers': function(finished, prefix) {	
		var wikiParser = new WikiParser();
		wikiParser.on("onAllArticlesLoaded", function(articlesCount){
			var quiz = wikiParser.getQuiz('Lance_Armstrong');
			var titles = {};
			var cnt = 0;
			for (var i = 0; i < quiz.answers.length; i ++) {
				console.log(quiz.answers[i].title);
				if (!titles[quiz.answers[i].title]){
					titles[quiz.answers[i].title] = true;
					cnt++;
				}
			}
		    equal(4, cnt, prefix+" duplicate titles ");
		    finished();		    
		});
		wikiParser.populateQuizCollection('Lance_Armstrong',10);
		
	}
};