package me.kavin.gwhpaladins.command.commands;

import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command{
	public Meme(){
	super(".meme");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		event.getChannel().sendMessage(getMeme()).queue();
		System.gc();
	}
	}
	private String getMeme() {
		ArrayList<String> memeurls = new ArrayList<String>();
		try{
		URI uri = new URI("https://api.imgflip.com/get_memes");
		URLConnection conn = uri.toURL().openConnection();
		conn.setRequestProperty("User-Agent",
		        "Meme Machine");
		JSONTokener tokener = new JSONTokener(conn.getInputStream());
		JSONObject root = new JSONObject(tokener);
		tokener = new JSONTokener(root.get("data").toString());
		root = new JSONObject(tokener);
		root.getJSONArray("memes").forEach( meme -> {
			JSONTokener parser = new JSONTokener(meme.toString());
			JSONObject data = new JSONObject(parser);
			memeurls.add(data.getString("url"));
		});
		}catch (Throwable t){
			t.printStackTrace();
		}
		return memeurls.get(new Random().nextInt(memeurls.size()));
	}
}
