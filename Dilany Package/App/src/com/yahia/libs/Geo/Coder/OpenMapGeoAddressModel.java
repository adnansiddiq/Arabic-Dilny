package com.yahia.libs.Geo.Coder;

public class OpenMapGeoAddressModel {	
		//country>>region>>county>>suburb>>
		private String country; 
		private String region;
		private String county; 
		 
		private String road;
		private String fullAdress;
		private double lat;
		private double lng;
		/*
		"lat": ,"lon": ,"display_name":"Mubarak Educational City Road, Zayed, Al-Sheikh Zayed, Markaz Tukh, Gizah, Egypt",
		"address":{
		"road":"Mubarak Educational City Road",
		"suburb":"Zayed","city":"Al-Sheikh Zayed",
		"county":"Markaz Tukh",
		"region":"Gizah",
		"country":"Egypt",
		"country_code":"eg"}
		}
	*/

}
