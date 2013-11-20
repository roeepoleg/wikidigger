package com.sason.tec.wikidigger.model;

import com.sason.tec.wikidigger.vo.QuizVO;

public class AppModel {
	
	private static AppModel mInstance;
	
	private QuizVO mCurrentQuiz;
	

	public QuizVO getCurrentQuiz() {
		return mCurrentQuiz;
	}

	public void setCurrentQuiz(QuizVO currentQuiz) {
		this.mCurrentQuiz = currentQuiz;
	}
	
	private AppModel (){
		
	}
	
	public static AppModel getInstance (){
		if (mInstance == null) {
			mInstance = new AppModel();
		}
		return mInstance;
	}

}