package com.sason.tec.wikidigger;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sason.tec.wikidigger.model.AppModel;
import com.sason.tec.wikidigger.tasks.GetQuizAsycTask;
import com.sason.tec.wikidigger.tasks.GetQuizAsycTask.IGetQuizTaskHandler;
import com.sason.tec.wikidigger.vo.ArticleVO;
import com.sason.tec.wikidigger.vo.QuizVO;

public class GameActivity extends Activity implements OnClickListener, IGetQuizTaskHandler{
	
	private WebView mQuestion;
	private Button mBtnAnswer0;
	private Button mBtnAnswer1;
	private Button mBtnAnswer2;
	private Button mBtnAnswer3;
	private ProgressBar mSivovator;
	
	private HashMap<Button, ArticleVO> mMap;
	private GetQuizAsycTask mGetQuiz;
	private boolean mAllowClicks = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		// hide action bar
		getActionBar().hide();
		
		// get reference to all views
		mSivovator = (ProgressBar) findViewById(R.id.progress);
		mQuestion = (WebView) findViewById(R.id.webQuestion);
		mBtnAnswer0 = (Button) findViewById(R.id.btnAnswer0);
		mBtnAnswer1 = (Button) findViewById(R.id.btnAnswer1);
		mBtnAnswer2 = (Button) findViewById(R.id.btnAnswer2);
		mBtnAnswer3 = (Button) findViewById(R.id.btnAnswer3);
		// set click listeners
		mBtnAnswer0.setOnClickListener(this);
		mBtnAnswer1.setOnClickListener(this);
		mBtnAnswer2.setOnClickListener(this);
		mBtnAnswer3.setOnClickListener(this);

		// load quiz
		onGetQuizResult(AppModel.getInstance().getCurrentQuiz());

	}


	@Override
	public void onClick(View v) {
		// prevent double/multiple clicks
		if (mAllowClicks) 
			mAllowClicks = false;
		else 
			return;
		
		final View view = v;
		ArticleVO selectedAnswer = mMap.get(view);
		if (selectedAnswer != null){
			
			// mark correct and incorrect answers
			if (selectedAnswer.getIsCorrect()){
				view.setBackgroundColor(getResources().getColor(R.color.GREEN));
			}
			else {
				view.setBackgroundColor(getResources().getColor(R.color.RED));
			}
			
			// return to normal style
		    Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		        	 view.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
		         } 
		    }, 2000); 
			
			getNextQuiz(selectedAnswer.getTitle());
		}
	}


	private void getNextQuiz(String title) {
		
		mSivovator.setVisibility(View.VISIBLE);
		
		if (mGetQuiz != null){
			mGetQuiz.cancel(true);
		}
		
		mGetQuiz = new GetQuizAsycTask(this);
		mGetQuiz.execute(title);
	}
	
	@Override
	public void onGetQuizResult(QuizVO quiz) {
		if (quiz != null){
			
			// set question
			mQuestion.getSettings().setJavaScriptEnabled(true);
			mQuestion.setWebViewClient(new WebViewClient()       
	        {
	             @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                view.loadUrl(url);
	                return true;
	            }
	             
	             public void onPageFinished(WebView view, String url) {
	                 // hide sivovator
	            	 mSivovator.setVisibility(View.INVISIBLE);
	             }
	        });
			mQuestion.loadUrl(quiz.getQuestion().getUrl());
			
			// create a random ordered array of answer
			ArticleVO [] arr = getRandomOrderedArray(quiz.getAnswers());
			
			// store in hash map for reference
			mMap = new HashMap<Button, ArticleVO>();
			mMap.put(mBtnAnswer0, arr[0]);
			mMap.put(mBtnAnswer1, arr[1]);
			mMap.put(mBtnAnswer2, arr[2]);
			mMap.put(mBtnAnswer3, arr[3]);
			
			// assign buttons values
			mBtnAnswer0.setText(arr[0].getTitle().replace('_', ' '));
			mBtnAnswer1.setText(arr[1].getTitle().replace('_', ' '));
			mBtnAnswer2.setText(arr[2].getTitle().replace('_', ' '));
			mBtnAnswer3.setText(arr[3].getTitle().replace('_', ' '));
			
			mAllowClicks = true;
		}
	}

	/*
	 * Randomly set the answers in the buttons
	 */
	private ArticleVO[] getRandomOrderedArray(ArticleVO[] answers) {

		ArticleVO[] mixed = new ArticleVO[4];
		int seed = (int) Math.ceil(Math.random()*3);
		for (int i = 0; i < mixed.length; i++) {
			mixed[i] = answers[i%seed];
		}
		
		return answers;
	}

}
