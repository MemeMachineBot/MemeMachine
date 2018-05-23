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

public class Reddit extends Command{
	
	Random rnd = new Random();
	String url = null;
	WebClient wc = new WebClient();
	
	public Reddit(){
		super(">reddit `Shows a post from the given reddit`");
	}
	
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.toLowerCase().startsWith(">reddit")){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String[] split = message.split(" ");
				if(split.length != 1)
					event.getChannel().sendMessage(getPost(split[1])).queue();
				else
					event.getChannel().sendMessage("`Please provide a subreddit as your argument like` \n.reddit <subreddit>").queue();
			}
		}).start();
		System.gc();
	}
	}
	private MessageEmbed getPost(String reddit) {
		try{
			EmbedBuilder meb = new EmbedBuilder();
			String data = wc.getPage("https://gateway.reddit.com/desktopapi/v1/subreddits/" + reddit + "?sort=hot").getWebResponse().getContentAsString();
			JSONTokener tokener = new JSONTokener(data);
			JSONObject root = new JSONObject(tokener);
			boolean found = false;
			int tries = 0;
			JSONObject posts = root.getJSONObject("posts");
			String[] keys = Arrays.copyOf(posts.keySet().toArray(), posts.keySet().size(), String[].class);
			while (!found && tries <= 5) {
				tries++;
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
