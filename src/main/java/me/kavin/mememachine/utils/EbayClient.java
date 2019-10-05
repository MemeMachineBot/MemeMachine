package me.kavin.mememachine.utils;

import kong.unirest.json.JSONObject;

import kong.unirest.Unirest;

public class EbayClient {

	private String client_id, client_secret, access_token;
	long expires_at;

	public EbayClient(String client_id, String client_secret) {
		this.client_id = client_id;
		this.client_secret = client_secret;
	}

	public String getAccessToken() {
		if (System.currentTimeMillis() + 5000 > expires_at) {
			try {
				JSONObject jObject = new JSONObject(Unirest.get("https://idauth.ebay.com/idauth/site/token?client_id="
						+ client_id + "&client_secret=" + client_secret
						+ "&grant_type=client_credentials&scope=https%3A%2F%2Fapi.ebay.com%2Foauth%2Fscope%2F%40public%20https%3A%2F%2Fapi.ebay.com%2Foauth%2Fscope%2Fbase%40public%20https%3A%2F%2Fapi.ebay.com%2Foauth%2Fscope%2Fexperience%40public")
						.asString().getBody());
				expires_at = System.currentTimeMillis() + jObject.getInt("expires_in") * 1000;
				return access_token = jObject.getString("access_token");
			} catch (Exception e) {
			}
		}
		return access_token;
	}

	public String getUserAgent() {
		return "eBayAndroid/5.0.0.34";
	}

}
