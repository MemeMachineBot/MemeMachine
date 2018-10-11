package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Lyrics extends Command {

	public Lyrics() {
		super(">lyrics", "`Allows you to search for a song's lyrics!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			String q = null;

			for (int i = getPrefix().length() + 1; i < message.length(); i++) {
				if (q == null)
					q = "";
				q += message.charAt(i);
			}
			
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Genius Lyrics Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONArray hits = new JSONObject(Unirest
					.get("https://genius.com/api/search/songs?q=" + URLEncoder.encode(q, "UTF-8")).asString().getBody())
							.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits");

			if (hits.length() == 0) {
				meb.addField("Error: song not found", "", false);
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			String url = "https://genius.com" + hits.getJSONObject(0).getJSONObject("result").getString("path");

			JSONObject lyricsData = new JSONObject(
					Jsoup.connect(url).get().select("head > meta:last-child").get(0).attr("content"))
							.getJSONObject("lyrics_data");

			String[] lines = lyricsData.getJSONObject("body").getString("html").replaceAll("\\<[^>]*>", "").split("\n");

			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (meb.getFields().size() >= 25) {
					event.getChannel().sendMessage(meb.build()).complete();
					meb.clearFields();
					meb.setTitle("");
					if (line.length() > 0)
						if (i + 1 == lines.length)
							meb.addField(StringEscapeUtils.unescapeHtml4(line) + "\n", "", false);
						else
							meb.addField(StringEscapeUtils.unescapeHtml4(line) + "\n", lines[++i], false);
				} else {
					if (line.length() > 0)
						if (i + 1 == lines.length)
							meb.addField(StringEscapeUtils.unescapeHtml4(line) + "\n", "", false);
						else
							meb.addField(StringEscapeUtils.unescapeHtml4(line) + "\n", lines[++i], false);
					else
						meb.addBlankField(false);
				}
			}

			if (meb.getFields().size() > 0)
				event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}