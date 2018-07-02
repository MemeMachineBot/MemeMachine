package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command {

	WebClient wc = new WebClient();

	String lastData = null;
	long lastUpdate = 0;

	public Meme() {
		super(">meme", "`Shows a random meme from /r/Memes`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage(getMeme()).complete();
	}

	private MessageEmbed getMeme() {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			if (System.currentTimeMillis() - lastUpdate > 300000)
				lastData = wc.getPage("https://www.reddit.com/r/memes/top/.json?sort=top&t=day&limit=250")
						.getWebResponse().getContentAsString();
			JSONObject root = new JSONObject(lastData);
			JSONArray posts = root.getJSONObject("data").getJSONArray("children");
			JSONObject post = posts.getJSONObject(ThreadLocalRandom.current().nextInt(posts.length()))
					.getJSONObject("data");
			meb.setTitle(post.getString("title"));
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setImage(Constants.GOOGLE_PROXY_IMAGE + URLEncoder.encode(post.getString("url"), "UTF-8"));
			String author = post.getString("author");
			meb.setAuthor(author, "https://www.reddit.com/user/" + author);
			meb.setFooter("\uD83D\uDC4D" + post.getInt("ups") + " | " + "\uD83D\uDCAC" + post.getInt("num_comments"),
					Main.api.getSelfUser().getAvatarUrl());
			return meb.build();
		} catch (Exception e) {
			return null;
		}
	}
}