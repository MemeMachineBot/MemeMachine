package me.kavin.mememachine.command.commands;

import org.json.JSONObject;
import org.json.XML;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Today extends Command {

	public Today() {
		super(">today", "`Shows a historic event that happened this day, that year`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			JSONObject jObject = XML
					.toJSONObject(Unirest.get("https://www.history.com/this-day-in-history/rss").asString().getBody())
					.getJSONObject("rss").getJSONObject("channel").getJSONObject("item");

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("On This Day");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField(jObject.getString("title"), "", false);
			meb.addField("More information can be found", "[here](" + jObject.getString("link") + ")", false);

			event.getChannel().sendMessage(meb.build()).complete();

		} catch (Exception e) {
		}
	}
}