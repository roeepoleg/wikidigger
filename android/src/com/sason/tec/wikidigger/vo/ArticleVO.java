package com.sason.tec.wikidigger.vo;

public class ArticleVO {
	
	private String title;
	private String url;
	private int distance;
	private boolean isCorrect = false;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String mTitle) {
		this.title = mTitle;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String mUrl) {
		this.url = mUrl;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int mDistance) {
		this.distance = mDistance;
	}
	public boolean getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

}
