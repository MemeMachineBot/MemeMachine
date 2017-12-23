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
	
	Random rnd = new Random();
	String url = null;
	
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
		try{
		URI uri = new URI("https://9gag.com/v1/group-posts/group/default/type/hot?after=aeevYMO%2CaDzE3o9%2CagYPr7q&c=" + ( rnd.nextInt(50) * 10));
		URLConnection conn = uri.toURL().openConnection();
		conn.setRequestProperty("User-Agent",
		        "Meme Machine");
		JSONTokener tokener = new JSONTokener(conn.getInputStream());
		JSONObject root = new JSONObject(tokener);
		tokener = new JSONTokener(root.get("data").toString());
		root = new JSONObject(tokener);
		root.getJSONArray("posts").forEach( meme -> {
			JSONTokener parser = new JSONTokener(meme.toString());
			JSONObject data = new JSONObject(parser);
			parser = new JSONTokener(data.get("images").toString());
			data = new JSONObject(parser);
			ArrayList<String> names = new ArrayList<String>();
			data.names().forEach( name -> {
				if (!names.contains(name.toString()))
					names.add(name.toString());
			});
			parser = new JSONTokener(data.get(names.get(new Random().nextInt(names.size()))).toString());
			data = new JSONObject(parser);
			url = data.getString("url");
		});
		}catch (Throwable t){
			t.printStackTrace();
		}
		return url;
	}
}
