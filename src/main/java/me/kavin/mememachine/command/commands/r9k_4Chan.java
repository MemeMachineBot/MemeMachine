package me.kavin.mememachine.command.commands;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import kong.unirest.Unirest;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class r9k_4Chan extends Command {

	long lastUpdate = 0;
	LongArrayList threadIds = new LongArrayList();
	private static final Random RANDOM = new Random();

	public r9k_4Chan() {
		super(">4chan", "`Allows you to browse r9k (aka ROBOT9001) on 4chan through discord!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			if (System.currentTimeMillis() - lastUpdate > 300000) {
				JSONArray pages = new JSONArray(Unirest.get("http://a.4cdn.org/r9k/threads.json").asString().getBody());

				threadIds.clear();

				for (int i = 0; i < pages.length(); i++) {
					JSONObject page = pages.getJSONObject(i);
					JSONArray threads = page.getJSONArray("threads");
					for (int j = 0; j < threads.length(); j++) {
						threadIds.add(threads.getJSONObject(j).getLong("no"));
					}
				}

				threadIds.removeElements(0, 2);

				lastUpdate = System.currentTimeMillis();
			}

			if (event.getTextChannel().isNSFW())
				try {
					EmbedBuilder meb = new EmbedBuilder();
					meb.setColor(ColorUtils.getRainbowColor(2000));

					JSONObject thread = new JSONObject(Unirest.get("http://a.4cdn.org/r9k/thread/"
							+ threadIds.getLong(RANDOM.nextInt(threadIds.size())) + ".json").asString().getBody());

					JSONObject post = thread.getJSONArray("posts").getJSONObject(0);

					meb.setTitle("R9K: " + post.getString("name"));
					String[] lines = post.getString("com").replaceAll("\\<[^>]*>", "").split("\n");

					for (int i = 0; i < lines.length; i++) {
						String line = lines[i];
						if (meb.getFields().size() >= 25) {
							event.getChannel().sendMessage(meb.build()).complete();
							meb.clearFields();
							meb.setTitle(" ");
							if (line.length() > 0)
								if (i + 1 == lines.length)
									meb.addField(StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(line), 255) + "\n", "", false);
								else
									meb.addField(StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(line), 255) + "\n",
											StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(lines[++i]), 1024), false);
						} else {
							if (line.length() > 0)
								if (i + 1 == lines.length)
									meb.addField(StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(line), 255) + "\n", "", false);
								else
									meb.addField(StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(line), 255) + "\n",
											StringUtils.abbreviate(StringEscapeUtils.unescapeHtml4(lines[++i]), 1024), false);
							else
								meb.addBlankField(false);
						}
					}

					if (post.has("tim")) {
						meb.setImage("http://i.4cdn.org/r9k/" + post.getLong("tim") + post.getString("ext"));
					}

					if (meb.getFields().size() > 0)
						event.getChannel().sendMessage(meb.build()).complete();
				} catch (Exception e) {
				}
			else {
				EmbedBuilder meb = new EmbedBuilder();

				meb.setTitle("Enable NSFW Channel");
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setImage("https://i.gyazo.com/9ac5c5a6ddf46b5c446853a7778b0cae.gif");

				event.getChannel().sendMessage(meb.build()).complete();

			}
		} catch (Exception e) {
		}
	}
}