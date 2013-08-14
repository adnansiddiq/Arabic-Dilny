package com.ksa.dilny.DataModel;

import com.google.android.maps.GeoPoint;

 public class ListItemModel {
	private String id;
	private String title;
	private String desc;
	private String image;
	private String thumb;
	private String ratings;
	private String country;
	//private JSONArray  price;
	private String address_line_1;
	private String address_line_2;
	private String lat;
	private String lng;
	private String distance;
	private String JsonItem;
	//private String priceMax;
	//private String PriceMin;
	private String db_id;
	private String reviewsHtml;
	private String phone;
	private String cat_id;
	private GeoPoint point;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getlat() {
		return lat;
	}
	public void setlat(String lat) {
		this.lat = lat;
	}
	public String getAddress_line_1() {
		return address_line_1;
	}
	public void setAddress_line_1(String address_line_1) {
		this.address_line_1 = address_line_1;
	}
	public String getAddress_line_2() {
		return address_line_2;
	}
	public void setAddress_line_2(String address_line_2) {
		this.address_line_2 = address_line_2;
	}
	public String getlng() {
		return lng;
	}
	public void setlng(String lng) {
		this.lng = lng;
	}
	  
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	 
	public String getRatings() {
		return ratings;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	/*public JSONArray getPrice() {
		return price;
	}
	public void setPrice(JSONArray price) {
		this.price = price;
		try {
			JSONObject object=price.getJSONObject(0);
			setPriceMin(object.getString("p"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject object=price.getJSONObject(1);
			setPriceMax(object.getString("p")); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public String getJsonItem() {
		return JsonItem;
	}
	public void setJsonItem(String jsonItem) {
		JsonItem = jsonItem;
	}
	/*
	public String getPriceMin() {
		return PriceMin;
	}
	public void setPriceMin(String priceMin) {
		PriceMin = priceMin;
	}
	public String getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}*/
	public String getDb_id() {
		return db_id;
	}
	public void setDb_id(String db_id) {
		this.db_id = db_id;
	}
	public String getReviewsHtml() {
		return reviewsHtml;
	}
	public void setReviewsHtml(String reviewsHtml) {
		this.reviewsHtml = reviewsHtml;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public GeoPoint getPoint() {
		return point;
	}
	public void setPoint(GeoPoint point) {
		this.point = point;
	}
	public String getCat_id() {
		return cat_id;
	}
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	 
	
 
}
