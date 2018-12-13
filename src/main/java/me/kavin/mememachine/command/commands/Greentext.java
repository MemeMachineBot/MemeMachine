package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.Unirest;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.reddit.ImagePostData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Greentext extends Command {

	ObjectArrayList<ImagePostData> lastData = new ObjectArrayList<>();
	long lastUpdate = 0;

	public Greentext() {
		super(">greentext", "`Shows a greentext meme`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage(getPost()).complete();
	}

	private MessageEmbed getPost() {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			if (System.currentTimeMillis() - lastUpdate > 300000) {

				JSONObject root = new JSONObject(
						Unirest.get("https://www.reddit.com/r/greentext/top/.json?sort=top&t=day&limit=250").asString()
								.getBody());

				JSONArray posts = root.getJSONObject("data").getJSONArray("children");

				lastData.clear();

				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i).getJSONObject("data");
					String img_url = post.getString("url");
					if (!post.getBoolean("over_18"))
						if (img_url.contains("i.redd.it"))
							lastData.add(new ImagePostData(post.getString("title"), post.getString("author"), img_url,
									post.getInt("num_comments"), post.getInt("ups")));
				}
				lastUpdate = System.currentTimeMillis();
			}

			ImagePostData imagePostData = lastData.get(ThreadLocalRandom.current().nextInt(lastData.size()));

			String author = imagePostData.author;

			meb.setTitle(imagePostData.title);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setImage(imagePostData.img_url);
			meb.setAuthor(author, "https://www.reddit.com/user/" + author);
			meb.setFooter(
					"\uD83D\uDC4D" + imagePostData.num_upvotes + " | " + "\uD83D\uDCAC" + imagePostData.num_comments,
					Main.api.getSelfUser().getAvatarUrl());

			return meb.build();
		} catch (Exception e) {
			return null;
		}
	}
}