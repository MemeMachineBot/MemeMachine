package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.MemeData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command {

	WebClient wc = new WebClient();

	ObjectArrayList<MemeData> lastData = new ObjectArrayList<>();
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
			if (System.currentTimeMillis() - lastUpdate > 300000) {

				JSONObject root = new JSONObject(
						wc.getPage("https://www.reddit.com/r/memes/top/.json?sort=top&t=day&limit=250").getWebResponse()
								.getContentAsString());

				JSONArray posts = root.getJSONObject("data").getJSONArray("children");

				lastData.clear();

				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i).getJSONObject("data");
					String img_url = post.getString("url");
					if(img_url.contains("i.redd.it"))
						lastData.add(new MemeData(post.getString("title"), post.getString("author"),
								img_url,
								post.getInt("num_comments"), post.getInt("ups")));
				}
				lastUpdate = System.currentTimeMillis();
			}

			MemeData memeData = lastData.get(ThreadLocalRandom.current().nextInt(lastData.size()));

			String author = memeData.author;

			meb.setTitle(memeData.title);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setImage(memeData.img_url);
			meb.setAuthor(author, "https://www.reddit.com/user/" + author);
			meb.setFooter("\uD83D\uDC4D" + memeData.num_upvotes + " | " + "\uD83D\uDCAC" + memeData.num_comments,
					Main.api.getSelfUser().getAvatarUrl());

			return meb.build();
		} catch (Exception e) {
			return null;
		}
	}
}