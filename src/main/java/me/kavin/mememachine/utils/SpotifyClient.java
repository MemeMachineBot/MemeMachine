package me.kavin.mememachine.utils;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class SpotifyClient {

	private String client_id, client_secret, access_token;
	long expires_at;

	public SpotifyClient(String client_id, String client_secret) {
		this.client_id = client_id;
		this.client_secret = client_secret;
	}

	public String getAccessToken() {
		if (System.currentTimeMillis() + 5000 > expires_at) {
			try {
				JSONObject jObject = new JSONObject(Unirest.post("https://accounts.spotify.com/api/token")
						.header("Authorization",
								"Basic " + Base64.encodeBase64String((client_id + ":" + client_secret).getBytes()))
						.header("Content-Type", "application/x-www-form-urlencoded")
						.field("grant_type", "client_credentials").asString().getBody());
				expires_at = System.currentTimeMillis() + jObject.getInt("expires_in") * 1000;
				return access_token = jObject.getString("access_token");
			} catch (Exception e) {
			}
		}
		return access_token;
	}

}
