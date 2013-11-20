package com.sason.tec.wikidigger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sason.tec.wikidigger.model.AppModel;
import com.sason.tec.wikidigger.tasks.GetQuizAsycTask;
import com.sason.tec.wikidigger.tasks.GetQuizAsycTask.IGetQuizTaskHandler;
import com.sason.tec.wikidigger.vo.QuizVO;

public class WelcomeActivity extends Activity implements OnClickListener {

	private Button mBtnStart;
	private GetQuizAsycTask mGetQuiz;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
//		ArticleVO a0 = new ArticleVO();
//		a0.setTitle("Taxonomy_(biology)");
//		a0.setUrl("http://en.wikipedia.org/wiki/Taxonomy_(biology)");
//		a0.setDistance(2);
//		
//		ArticleVO a1 = new ArticleVO();
//		a1.setTitle("Carl_Linnaeus");
//		a1.setUrl("http://en.wikipedia.org/wiki/Carl_Linnaeus");
//		a1.setDistance(3);
//		
//		ArticleVO a2 = new ArticleVO();
//		a2.setTitle("Provinces_of_Sweden");
//		a2.setUrl("http://en.wikipedia.org/wiki/Provinces_of_Sweden");
//		a2.setDistance(6);
//		
//		ArticleVO a3 = new ArticleVO();
//		a3.setTitle("Petty_kingdom");
//		a3.setUrl("http://en.wikipedia.org/wiki/Petty_kingdom");
//		a3.setDistance(9);
//		
//		ArticleVO q = new ArticleVO();
//		q.setTitle("Lambeosaurus");
//		q.setUrl("http://en.wikipedia.org/wiki/Lambeosaurus");
//		q.setDistance(0);
//		
//		QuizVO quiz = new QuizVO();
//		quiz.setQuestion(q);
//		ArticleVO []  arr = {a0,a1,a2,a3};
//		quiz.setAnswers(arr);
//		
//		String jsonStr = (new Gson()).toJson(quiz);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		mBtnStart = (Button) findViewById(R.id.btnStart);
		mBtnStart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (mGetQuiz != null){
			mGetQuiz.cancel(true);
		}
		
		mGetQuiz = new GetQuizAsycTask(new IGetQuizTaskHandler() {
			
			public void onGetQuizResult(QuizVO result) {

				AppModel.getInstance().setCurrentQuiz(result);
				Intent intent = new Intent(getBaseContext(), GameActivity.class);
			    startActivity(intent);
			}
		});
		// first questions is randomly selected by the server
		mGetQuiz.execute("");
		
	}
}
