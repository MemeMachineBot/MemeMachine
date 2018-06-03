package me.kavin.mememachine.lists;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.Multithreading;

public class Api {

	public static void loop() {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				while (true) {
					dbl();
					try {
						Thread.sleep(300000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}

	public static void dbl() {
		JSONObject obj = new JSONObject().put("server_count", Main.api.getGuilds().size());
		try {
			Unirest.post("https://discordbots.org/api/bots/" + Main.api.getSelfUser().getId() + "/stats")
					.header("Authorization", Constants.DBL_TOKEN).header("Content-Type", "application/json")
					.body(obj.toString()).asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
