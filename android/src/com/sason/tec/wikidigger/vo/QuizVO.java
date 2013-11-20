package com.sason.tec.wikidigger.vo;

public class QuizVO {
	
	private ArticleVO question;
	private  ArticleVO [] answers;
	
	public ArticleVO getQuestion() {
		return question;
	}
	public void setQuestion(ArticleVO mQuestion) {
		this.question = mQuestion;
	}
	public ArticleVO [] getAnswers() {
		return answers;
	}
	public void setAnswers(ArticleVO [] mAnswers) {
		this.answers = mAnswers;
	}

}
