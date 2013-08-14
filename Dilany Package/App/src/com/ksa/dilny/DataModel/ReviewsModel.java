package com.ksa.dilny.DataModel;

 public class ReviewsModel {
	private String user_name;
	private String root;
	private String user_logo_image;
	private String review;
	private String id;
	private String date_added;
	private String ip;
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDate_added() {
		return date_added;
	}
	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getUser_logo_image() {
		return user_logo_image;
	}
	public void setUser_logo_image(String user_logo_image) {
		this.user_logo_image = user_logo_image;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
 

}
