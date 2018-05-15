package me.kavin.gwhpaladins.command.commands;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Yt extends Command{
	
	private String result = null;
	private String q = null;
	
	public Yt(){
	super(".yt `Allows you to search youtube`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.toUpperCase().startsWith(".yt".toUpperCase())){
		
		q = null;
		
		if(message.length() > 4) {
			q = "";
			for (int i = 4;i < message.length();i++)
				q += message.charAt(i);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				event.getChannel().sendMessage(getSearch(q)).queue();
			}
		}).start();
		System.gc();
	}
	}
	private String getSearch(String q) {
		try {
			result = null;
			String url = "https://www.googleapis.com/youtube/v3/search/?" + "q=" + q.replace(" ", "%20") + "&type=video,channel&part=snippet&key=AIzaSyAMkHWnLNAvpKte-XA9nh3RheX7lFn_dNM";
			JSONTokener tokener = new JSONTokener(Unirest.get(url).header("referer", "https://google-developers.appspot.com").asString().getBody());
			JSONObject root = new JSONObject(tokener);
			if (!root.has("items"))
				return "`No results found for " + q + "`";
			root.getJSONArray("items").forEach( item -> {
				JSONTokener itemTokener = new JSONTokener(item.toString());
				JSONObject body = new JSONObject(itemTokener);
				boolean video = body.getJSONObject("id").getString("kind").equals("youtube#video");
				if(video) {
					if(result != null)
						result += "\n`" + body.getJSONObject("snippet").getString("title") +"` https://www.youtube.com/watch?v=" + body.getJSONObject("id").getString("videoId");
					else
						result = "`" + body.getJSONObject("snippet").getString("title") +"` https://www.youtube.com/watch?v=" + body.getJSONObject("id").getString("videoId");
				} else {
					if(result != null)
						result += "\n`" + body.getJSONObject("snippet").getString("title") +"` https://www.youtube.com/channel/" + body.getJSONObject("id").getString("channelId");
					else
						result = "`" + body.getJSONObject("snippet").getString("title") +"` https://www.youtube.com/channel/" + body.getJSONObject("id").getString("channelId");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result == null ? "Error" : result;
	}
}
