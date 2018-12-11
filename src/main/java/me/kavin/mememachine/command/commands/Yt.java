package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Yt extends Command {

	public Yt() {
		super(">yt", "`Allows you to search youtube`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {

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
			event.getChannel().sendMessage(meb.build()).complete();
			return;
		}

		event.getChannel().sendMessage(getSearch(q)).complete();
	}

	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/youtube/v3/search/?" + "safeSearch=moderate" + "&regionCode=US"
					+ "&q=" + URLEncoder.encode(q, "UTF-8") + "&type=video,channel&part=snippet&key="
					+ Constants.GOOGLE_API_KEY;
			JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());
			meb.setTitle("Youtube Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`" + "\n", false);
				return meb.build();
			}
			root.getJSONArray("items").forEach(item -> {
				JSONObject body = new JSONObject(item.toString());
				boolean video = body.getJSONObject("id").getString("kind").equals("youtube#video");
				if (video) {
					meb.addField('`' + body.getJSONObject("snippet").getString("title") + '`',
							"https://www.youtube.com/watch?v=" + body.getJSONObject("id").getString("videoId") + "\n",
							false);
				} else {
					meb.addField('`' + body.getJSONObject("snippet").getString("title") + '`',
							"https://www.youtube.com/channel/" + body.getJSONObject("id").getString("channelId") + "\n",
							false);
				}
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}