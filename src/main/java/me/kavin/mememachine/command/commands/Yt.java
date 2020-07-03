package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Yt extends Command {

	public Yt() {
		super(">yt", "`Allows you to search youtube`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {

		String q = null;

		for (int i = getPrefix().length() + 1; i < message.length(); i++) {
			if (q == null)
				q = "";
			q += message.charAt(i);
		}

		if (q == null) {
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setTitle("Error: No Arguments provided!");
			meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
			event.getChannel().sendMessage(meb.build()).queue();
			return;
		}

		EmbedBuilder meb = new EmbedBuilder();

		String url = "https://invidious.snopyta.org/api/v1/search?type=all&fields=authorUrl,authorThumbnails,author,videoThumbnails,videoId,title,type&q="
				+ URLEncoder.encode(q, "UTF-8");
		JSONArray jArray = Unirest.get(url).asJson().getBody().getArray();

		meb.setTitle("Youtube Search: " + q);
		meb.setColor(ColorUtils.getRainbowColor(2000));

		if (jArray.length() == 0)
			meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`" + "\n", false);
		else
			for (int i = 0; i < 10 && i < jArray.length(); i++) {
				JSONObject body = jArray.getJSONObject(i);
				if (body.getString("type").equals("video")) {
					meb.addField('`' + body.getString("title") + '`',
							"https://www.youtube.com/watch?v=" + body.getString("videoId") + "\n", false);
					if (i == 0)
						meb.setImage(Constants.IMAGE_PROXY_URL
								+ URLEncoder.encode(body.getJSONArray("videoThumbnails").getJSONObject(1)
										.getString("url").replace("invidious.snopyta.org", "i.ytimg.com"), "UTF-8"));
				} else if (body.getString("type").equals("channel")) {
					meb.addField('`' + body.getString("author") + '`',
							"https://www.youtube.com" + body.getString("authorUrl") + "\n", false);
					if (i == 0) {
						JSONArray thumbnails = body.getJSONArray("authorThumbnails");
						meb.setImage(Constants.IMAGE_PROXY_URL + URLEncoder
								.encode(thumbnails.getJSONObject(thumbnails.length() - 1).getString("url"), "UTF-8"));
					}
				}
			}

		event.getChannel().sendMessage(meb.build()).queue();
	}
}