package com.sason.tec.wikidigger.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sason.tec.wikidigger.vo.QuizVO;

public class GetQuizAsycTask extends AsyncTask<String, Void, QuizVO> {

	private static final String jsonDummy = "{\"answers\":[{\"url\":\"http://en.wikipedia.org/wiki/Taxonomy_(biology)\",\"title\":\"Taxonomy_(biology)\",\"distance\":2,\"isCorrect\":true},{\"url\":\"http://en.wikipedia.org/wiki/Carl_Linnaeus\",\"title\":\"Carl_Linnaeus\",\"distance\":3,\"isCorrect\":false},{\"url\":\"http://en.wikipedia.org/wiki/Provinces_of_Sweden\",\"title\":\"Provinces_of_Sweden\",\"distance\":6,\"isCorrect\":false},{\"url\":\"http://en.wikipedia.org/wiki/Petty_kingdom\",\"title\":\"Petty_kingdom\",\"distance\":9,\"isCorrect\":false}],\"question\":{\"url\":\"http://en.wikipedia.org/wiki/Lambeosaurus\",\"title\":\"Lambeosaurus\",\"distance\":0,\"isCorrect\":false}}";
	//{"answers":[{"url":"http://en.wikipedia.org/wiki/Taxonomy_(biology)","title":"Taxonomy_(biology)","distance":2},{"url":"http://en.wikipedia.org/wiki/Carl_Linnaeus","title":"Carl_Linnaeus","distance":3},{"url":"http://en.wikipedia.org/wiki/Provinces_of_Sweden","title":"Provinces_of_Sweden","distance":6},{"url":"http://en.wikipedia.org/wiki/Petty_kingdom","title":"Petty_kingdom","distance":9}],"question":{"url":"http://en.wikipedia.org/wiki/Lambeosaurus","title":"Lambeosaurus","distance":0}}

	private static final String REST_API_PATH = "http://secure-spire-2766.herokuapp.com/quiz";

	public IGetQuizTaskHandler handler;

	/**
	 * 
	 * @param handler
	 */
	public GetQuizAsycTask (IGetQuizTaskHandler handler)
	{
		if (handler != null)
		{
			this.handler = handler;
		}
	}

	@Override
	protected QuizVO doInBackground(String... params) {

		QuizVO quiz = null;

		if (params != null &&
				params.length > 0) {
			String url = REST_API_PATH+ params[0];


			// make Http GET call to the server with the given URL
			// Prepare a request object
			HttpGet httpget = new HttpGet(url); 
			HttpClient httpclient = new DefaultHttpClient();

			// Execute the request
			HttpResponse response;
			/*try {
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i("GetQuizAsycTask",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            instream.close();

	            if (result != null){
	            	quiz = (new Gson()).fromJson(result, QuizVO.class);
	            }
	        }


	    } catch (Exception e) {}
			 */
		}
		quiz = (new Gson()).fromJson(jsonDummy, QuizVO.class);
		return quiz;
	}


	protected void onPostExecute(QuizVO result) {

		if (result != null){
			handler.onGetQuizResult(result);
		}
		handler = null;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public interface IGetQuizTaskHandler {

		public void onGetQuizResult (QuizVO result);

	}
}


