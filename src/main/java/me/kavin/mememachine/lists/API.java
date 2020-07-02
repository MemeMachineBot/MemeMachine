package me.kavin.mememachine.lists;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.consts.Constants;

public class API {

	public static void loop() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				dbl();
				b4d();
				dcbotspw();
			}
		}, 0, TimeUnit.MINUTES.toMillis(5));
	}

	public static void dcbotspw() {
		JSONObject obj = new JSONObject().put("server_count", Main.api.getGuilds().size());
		try {
			Unirest.post(
					"https://bots.discord.pw/api/bots/" + Main.api.getShards().get(0).getSelfUser().getId() + "/stats")
					.header("Authorization", Constants.DISCORD_BOTS_PW_TOKEN).header("Content-Type", "application/json")
					.body(obj.toString()).asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void b4d() {
		JSONObject obj = new JSONObject().put("server_count", Main.api.getGuilds().size());
		try {
			Unirest.post("https://botsfordiscord.com/api/bot/" + Main.api.getShards().get(0).getSelfUser().getId())
					.header("Authorization", Constants.B4D_TOKEN).header("Content-Type", "application/json")
					.body(obj.toString()).asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public static void dbl() {
		JSONObject obj = new JSONObject().put("server_count", Main.api.getGuilds().size());
		try {
			Unirest.post(
					"https://discordbots.org/api/bots/" + Main.api.getShards().get(0).getSelfUser().getId() + "/stats")
					.header("Authorization", Constants.DBL_TOKEN).header("Content-Type", "application/json")
					.body(obj.toString()).asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
