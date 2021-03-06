package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Google extends Command {

	public Google() {
		super(">google", "`Allows you to search google`");
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
			event.getChannel().sendMessage(meb.build()).queue();
			return;
		}

		event.getChannel().sendMessage(getSearch(q)).queue();
	}

	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&" + "q="
					+ URLEncoder.encode(q, "UTF-8")
					+ "&cx=008677437472124065250%3Ajljeb59kuse&imgSize=small&lr=lang_en&num=5&key="
					+ Constants.GOOGLE_API_KEY;
			JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());
			meb.setTitle("Google Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`" + "\n", false);
				return meb.build();
			}
			root.getJSONArray("items").forEach(item -> {
				JSONObject body = new JSONObject(item.toString());
				meb.addField('`' + body.getString("title") + '`', body.getString("link") + "\n", false);
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}