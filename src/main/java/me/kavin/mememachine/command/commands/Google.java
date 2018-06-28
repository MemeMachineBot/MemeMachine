package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Google extends Command {

	public Google() {
		super(">google", "`Allows you to search google`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		if (message.toLowerCase().startsWith(getPrefix())) {

			String q = null;

			for (int i = getPrefix().length() + 1; i < message.length(); i++) {
				if(q == null)
					q = "";
				q += message.charAt(i);
			}

			event.getChannel().sendMessage(getSearch(q)).complete();
		}
	}

	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&" + "q="
					+ URLEncoder.encode(q, "UTF-8")
					+ "&cx=008677437472124065250%3Ajljeb59kuse&imgSize=small&lr=lang_en&num=5&key="
					+ Constants.GOOGLE_API_KEY;
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			meb.setTitle("Google Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				return meb.build();
			}
			root.getJSONArray("items").forEach(item -> {
				JSONTokener itemTokener = new JSONTokener(item.toString());
				JSONObject body = new JSONObject(itemTokener);
				meb.addField('`' + body.getString("title") + '`', body.getString("link"), true);
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}