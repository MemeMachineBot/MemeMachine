package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;
import java.text.DecimalFormat;

import org.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Subs extends Command {

	private DecimalFormat df = new DecimalFormat("#,###");

	public Subs() {
		super(">subs", "`Checks the number of subscribers the given youtuber has`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			String q = null;

			if (message.length() > getPrefix().length()) {
				q = "";
				for (int i = getPrefix().length() + 1; i < message.length(); i++)
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

			String url = "https://www.googleapis.com/youtube/v3/search/?" + "safeSearch=moderate" + "&regionCode=US"
					+ "&q=" + URLEncoder.encode(q, "UTF-8") + "&type=channel&part=snippet&key="
					+ Constants.GOOGLE_API_KEY;

			JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("YouTube Subscribers");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			meb.addField("`Channel: `", "https://www.youtube.com/channel/"
					+ root.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("channelId") + "\n",
					false);

			JSONObject subs = new JSONObject(Unirest
					.get("https://www.googleapis.com/youtube/v3/channels?part=statistics&id="
							+ root.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("channelId")
							+ "&fields=items/statistics/subscriberCount&key=" + Constants.GOOGLE_API_KEY)
					.asString().getBody());

			meb.addField("`Subscribers: `", df.format(
					subs.getJSONArray("items").getJSONObject(0).getJSONObject("statistics").getInt("subscriberCount"))
					+ "\n", false);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}