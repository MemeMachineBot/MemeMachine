package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.reddit.TextPostData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Tifu extends Command {

	ObjectArrayList<TextPostData> lastData = new ObjectArrayList<>();
	long lastUpdate = 0;

	public Tifu() {
		super(">tifu", "`Today I f*cked up!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			if (System.currentTimeMillis() - lastUpdate > 300000) {

				JSONObject root = new JSONObject(Unirest
						.get("https://www.reddit.com/r/tifu/top/.json?sort=top&t=day&limit=250").asString().getBody());

				JSONArray posts = root.getJSONObject("data").getJSONArray("children");

				lastData.clear();

				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = posts.getJSONObject(i).getJSONObject("data");
					if (!post.getBoolean("over_18"))
						lastData.add(new TextPostData(post.getString("title"), post.getString("author"),
								post.getString("selftext"), post.getInt("num_comments"), post.getInt("ups")));
				}
				lastUpdate = System.currentTimeMillis();
			}

			EmbedBuilder meb = new EmbedBuilder();

			TextPostData textPostData = lastData.get(ThreadLocalRandom.current().nextInt(lastData.size()));

			String author = textPostData.author;

			meb.setTitle(textPostData.title);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setAuthor(author, "https://www.reddit.com/user/" + author);
			meb.setFooter(
					"\uD83D\uDC4D" + textPostData.num_upvotes + " | " + "\uD83D\uDCAC" + textPostData.num_comments,
					event.getJDA().getSelfUser().getAvatarUrl());
			meb.setDescription(StringUtils.abbreviate(textPostData.data, 2048));

			event.getChannel().sendMessage(meb.build()).queue();

		} catch (Exception e) {
		}
	}
}