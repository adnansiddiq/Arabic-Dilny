package com.yahia.libs.Geo.Coder;

public class GoogleGeoAddressModel {
	public static  final String CODE_COUNTRY="[\"country\",\"political\"]";
	public static  final String CODE_LOCALITY="[\"locality\",\"political\"]";
	public static  final String CODE_SUBLOCALITY="[\"sublocality\",\"political\"]";
	public static  final String CODE_STREET_NUMBER="[\"street_number\"]";
	public static  final String CODE_ADMIN_LEVEL_1="[\"administrative_area_level_1\",\"political\"]";
	public static  final String CODE_ADMIN_LEVEL_2="[\"administrative_area_level_2\",\"political\"]";
	public static  final String CODE_ADMIN_LEVEL_3="[\"administrative_area_level_3\",\"political\"]";
	public static  final String CODE_ROUTE="[\"route\"]";
	public static  final String CODE_PSTAL_CODE="[\"postal_code\"]";
	
 
 
	
	private String country; 
	private String locality; 
	private String subLocality; 
	private String street_number;
	private String route; 
	private String adminLevel1;
	private String adminLevel2; 
	private String adminLevel3;  
	private String postalCcode;
	private String fullAdress;
	private double lat;
	private double lng;
	 
	                         
	                    
	                      
	                      
	public GoogleGeoAddressModel(){};  
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	} 
	public String getPostalCcode() {
		return postalCcode;
	}
	public void setPostalCcode(String postalCcode) {
		if(postalCcode.equals("null")|| postalCcode ==null){
			this.postalCcode = null;
		}else {
			this.postalCcode = postalCcode;
		}
		
	}
	public String getAdminLevel3() {
		return adminLevel3;
	}
	public void setAdminLevel3(String adminLevel3) { 
		if(adminLevel3.equals("null")|| adminLevel3 ==null){
			this.adminLevel3 = null;
		}else {
			this.adminLevel3 = adminLevel3;
		}
	}
	public String getAdminLevel2() {
		return adminLevel2;
	}
	public void setAdminLevel2(String adminLevel2) {
		if(adminLevel2.equals("null")|| adminLevel2 ==null){
			this.adminLevel2 = null;
		}else {
			this.adminLevel2 = adminLevel2;
		}
	}
	public String getAdminLevel1() {
		return adminLevel1;
	}
	public void setAdminLevel1(String adminLevel1) {
		if(adminLevel1.equals("null")|| adminLevel1 ==null){
			this.adminLevel1 = null;
		}else {
			this.adminLevel1 = adminLevel1;
		}
	}
	public String getRoute() {
		return route;
		
	}
	public void setRoute(String route) { 
		if(route.equals("null")|| route ==null){
			this.route = null;
		}else {
			this.route = route;
		}
	}
	public String getStreet_number() {
		return street_number;
	}
	public void setStreet_number(String street_number) {		 
		if(street_number.equals("null")|| street_number ==null){
			this.street_number = null;
		}else {
			this.street_number = street_number;
		}
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		 
		if(locality.equals("null") || locality ==null){
			this.locality = null;
		}else {
			this.locality = locality;
		}
	}

	public String getFullAdress() {
		return fullAdress;
	}

	public void setFullAdress(String fullAdress) {
		this.fullAdress = fullAdress;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getSubLocality() {
		return subLocality;
	}

	public void setSubLocality(String subLocality) {
		this.subLocality = subLocality;
	}

	 
}
