package me.kavin.mememachine.lists;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import me.kavin.mememachine.Main;

public class Api {
	
	private static final String DBL_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjQ0NTgwMDUwNTMxNTQyNjMxNSIsImJvdCI6dHJ1ZSwiaWF0IjoxNTI2ODk0NTM5fQ.P4FT71CVwGqWoAgU9Lgo-xqlgllFy8BdAZjhWZKRnNE";
	
	public static void loop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					dbl();
					try {
						Thread.sleep(300000);
					} catch (InterruptedException e) { }
				}
			}
		}).start();
	}
	
	public static void dbl() {
		JSONObject obj = new JSONObject()
			    .put("server_count", Main.api.getGuilds().size());
			    try {
					Unirest.post("https://discordbots.org/api/bots/" + Main.api.getSelfUser().getId() + "/stats")
					        .header("Authorization", DBL_TOKEN)
					        .header("Content-Type", "application/json")
					        .body(obj.toString())
					        .asJson();
				} catch (UnirestException e) {
					e.printStackTrace();
				}
	}
}
