package me.kavin.mememachine.command.commands;

import java.awt.Color;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Yt extends Command{
	
	private String q = null;
	
	public Yt(){
	super(">yt", "`Allows you to search youtube`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.toLowerCase().startsWith(getPrefix())){
		
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
	}
	}
	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/youtube/v3/search/?" + "safeSearch=moderate" + "&regionCode=US" + "&q=" + q.replace(" ", "%20") + "&type=video,channel&part=snippet&key=AIzaSyBjdQtArYb2oOdPIgPy5NQVW7CgBTZsi5E";
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			meb.setTitle("Youtube Search: " + q);
			meb.setColor(getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				return meb.build();
			}
			root.getJSONArray("items").forEach( item -> {
				JSONTokener itemTokener = new JSONTokener(item.toString());
				JSONObject body = new JSONObject(itemTokener);
				boolean video = body.getJSONObject("id").getString("kind").equals("youtube#video");
				if(video) {
					meb.addField('`' + body.getJSONObject("snippet").getString("title") + '`', "https://www.youtube.com/watch?v=" + body.getJSONObject("id").getString("videoId"), true);
				} else {
					meb.addField('`' + body.getJSONObject("snippet").getString("title") + '`', "https://www.youtube.com/channel/" + body.getJSONObject("id").getString("channelId"), true);
				}
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
	
}
