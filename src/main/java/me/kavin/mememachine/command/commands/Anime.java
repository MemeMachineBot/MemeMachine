package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

			if (q == null) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Error: No Arguments provided!");
				meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("9Anime Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			Document doc = Jsoup.parse(new JSONObject(
					Unirest.get("https://www1.9anime.to/ajax/film/search?keyword=" + URLEncoder.encode(q, "UTF-8"))
							.asString().getBody()).getString("html"));

			Elements data = doc.select("body > div:nth-child(n) > div > a");

			if (data.size() > 0) {
				for (Element element : data) {
					meb.addField("`" + element.text() + "`", "https://www1.9anime.to" + element.attr("href") + "\n",
							false);
				}
			} else {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
			}

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}