package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Anime extends Command {

	public Anime() {
		super(">anime", "`Allows you to search for anime!`");
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

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Kiss Anime Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONObject jObject = new JSONObject(
					Unirest.post("https://kiss-anime.io/search.ajax?query=" + URLEncoder.encode(q, "UTF-8")).asString()
							.getBody());

			if (jObject.getString("results").length() > 0) {
				for (Element element : Jsoup.parse(jObject.getString("results")).getElementsByClass("name")) {
					meb.addField("`" + element.text() + "`", "https://kiss-anime.io" + element.attr("href") + "\n",
							false);
				}
			} else {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
			}

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}