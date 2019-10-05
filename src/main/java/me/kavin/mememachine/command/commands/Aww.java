package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.reddit.ImagePostData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Aww extends Command {

	ObjectArrayList<ImagePostData> lastData = new ObjectArrayList<>();
	long lastUpdate = 0;

	public Aww() {
		super(">aww", "`Shows a cute image`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		EmbedBuilder meb = new EmbedBuilder();
		if (System.currentTimeMillis() - lastUpdate > 300000) {

			JSONObject root = new JSONObject(Unirest
					.get("https://www.reddit.com/r/aww/top/.json?sort=top&t=day&limit=250").asString().getBody());

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
		meb.setFooter("\uD83D\uDC4D" + imagePostData.num_upvotes + " | " + "\uD83D\uDCAC" + imagePostData.num_comments,
				event.getJDA().getSelfUser().getAvatarUrl());

		event.getChannel().sendMessage(meb.build()).queue();
	}
}