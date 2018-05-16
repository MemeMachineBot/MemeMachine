package me.kavin.gwhpaladins.command.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.gargoylesoftware.htmlunit.WebClient;
import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command{
	
	Random rnd = new Random();
	String url = null;
	WebClient wc = new WebClient();
	
	public Meme(){
		super(".meme `Shows a random meme from imgur`");
	}
	
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(".meme")){
		new Thread(new Runnable() {
			@Override
			public void run() {
				event.getChannel().sendMessage(getMeme()).queue();
			}
		}).start();
		System.gc();
	}
	}
	private MessageEmbed getMeme() {
		try{
			EmbedBuilder meb = new EmbedBuilder();
			String data = wc.getPage("https://gateway.reddit.com/desktopapi/v1/subreddits/memes?sort=hot").getWebResponse().getContentAsString();
			JSONTokener tokener = new JSONTokener(data);
			JSONObject root = new JSONObject(tokener);
			boolean found = false;
			JSONObject posts = root.getJSONObject("posts");
			String[] keys = Arrays.copyOf(posts.keySet().toArray(), posts.keySet().size(), String[].class);
			while (!found) {
				JSONObject post = posts.getJSONObject(keys[ThreadLocalRandom.current().nextInt(keys.length)]);
				if (post.getBoolean("isLocked"))
					continue;
				found = true;
				meb.setTitle(post.getString("title"));
				meb.setColor(getRainbowColor(2000));
				meb.setImage(post.getJSONObject("media").getString("content"));
				meb.setAuthor(post.getString("author"));
				String s = "👍" + post.getInt("score") + " | " + "💬" + post.getInt("numComments");
				meb.setDescription(s);
			}
			return meb.build();
		} catch (Throwable t){
			t.printStackTrace();
		}
		return null;
	}
	
	public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
	
}
