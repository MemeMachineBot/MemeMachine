package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import kong.unirest.Unirest;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Define extends Command {

	public Define() {
		super(">define", "`Gets the definition of a term from urbandictionary.com`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		String q = null;

		for (int i = getPrefix().length() + 1; i < message.length(); i++) {
			if (q == null)
				q = "";
			q += message.charAt(i);
		}

		if (event.getTextChannel().isNSFW())
			event.getChannel().sendMessage(getSearch(q)).complete();
		else {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Enable NSFW Channel");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setImage("https://i.gyazo.com/9ac5c5a6ddf46b5c446853a7778b0cae.gif");

			event.getChannel().sendMessage(meb.build()).complete();

		}
	}

	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "http://api.urbandictionary.com/v0/autocomplete-extra?&term=" + URLEncoder.encode(q, "UTF-8");
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			meb.setTitle("Urban Dictionary Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			JSONArray jArray = root.getJSONArray("results");
			if (jArray.length() == 0) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", false);
				return meb.build();
			}
			JSONObject body = jArray.getJSONObject(0);
			String term = body.getString("term");
			meb.setDescription(StringUtils
					.abbreviate('`' + term + '`' + '\n' + getDescription(term, body.getString("preview")), 2048));
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getDescription(String q, String find) {
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(
					Unirest.get("http://api.urbandictionary.com/v0/define?term=" + q.replace(" ", "%20")).asString()
							.getBody());
		} catch (Exception e) {
		}
		JSONArray jArray = jObject.getJSONArray("list");
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject obj = jArray.getJSONObject(i);
			String s = obj.getString("definition").replaceAll("\\[(.*?)\\]", "$1");
			if (s.startsWith(find) || s.equals(find))
				return s;
		}
		return jArray.getJSONObject(0).getString("definition").replaceAll("\\[(.*?)\\]", "$1");
	}
}