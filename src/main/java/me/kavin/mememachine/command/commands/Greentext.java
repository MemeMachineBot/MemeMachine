package me.kavin.mememachine.command.commands;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.gargoylesoftware.htmlunit.WebClient;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Greentext extends Command {

	WebClient wc = new WebClient();

	public Greentext() {
		super(">greentext", "`Shows a greentext meme`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())) {
			event.getChannel().sendMessage(getMeme()).complete();
		}
	}

	private MessageEmbed getMeme() {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String data = wc.getPage("https://gateway.reddit.com/desktopapi/v1/subreddits/greentext?sort=hot")
					.getWebResponse().getContentAsString();
			int tries = 0;
			JSONTokener tokener = new JSONTokener(data);
			JSONObject root = new JSONObject(tokener);
			boolean found = false;
			JSONObject posts = root.getJSONObject("posts");
			String[] keys = Arrays.copyOf(posts.keySet().toArray(), posts.keySet().size(), String[].class);
			while (!found && tries <= 5) {
				JSONObject post = posts.getJSONObject(keys[ThreadLocalRandom.current().nextInt(keys.length)]);
				if (post.getBoolean("isLocked") || post.isNull("media")
						|| !post.getJSONObject("media").has("content")) {
					tries++;
					continue;
				}
				found = true;
				meb.setTitle(post.getString("title"));
				meb.setColor(ColorUtils.getRainbowColor(2000));
				meb.setImage(post.getJSONObject("media").getString("content"));
				String author = post.getString("author");
				meb.setAuthor(author, "https://www.reddit.com/user/" + author);
				meb.setFooter(
						"\uD83D\uDC4D" + post.getInt("score") + " | " + "\uD83D\uDCAC" + post.getInt("numComments"),
						Main.api.getSelfUser().getAvatarUrl());
			}
			return meb.build();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}