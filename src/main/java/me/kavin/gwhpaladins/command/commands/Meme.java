package me.kavin.gwhpaladins.command.commands;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.json.JSONTokener;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command{
	
	Random rnd = new Random();
	String url = null;
	
	public Meme(){
	super(".meme");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		new Thread(new Runnable() {
			@Override
			public void run() {
				event.getChannel().sendMessage(getMeme()).queue();
			}
		}).start();
		System.gc();
	}
	}
	private String getMeme() {
		try{
			String clientID = "5dcea4b044bd1cc";
			URL url = new URL("https://api.imgur.com/3/g/memes/time/1");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
	        conn.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");
		JSONTokener tokener = new JSONTokener(conn.getInputStream());
		JSONObject root = new JSONObject(tokener);
		boolean found = false;
		while (!found) {
			String data = root.getJSONArray("data").get(ThreadLocalRandom.current().nextInt(root.getJSONArray("data").length())).toString();
			JSONTokener tokener1 = new JSONTokener(data);
			JSONObject root1 = new JSONObject(tokener1);
			if(!root1.get("privacy").equals("hidden")) {
				found = true;
				root = root1;
			}
		}
		return root.get("link").toString();
		}catch (Throwable t){
			t.printStackTrace();
		}
		return null;
	}
}
