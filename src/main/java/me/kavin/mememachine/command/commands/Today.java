package me.kavin.mememachine.command.commands;

import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Today extends Command {

	public Today() {
		super(">today", "`Shows a historic event that happened this day, that year`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			SyndFeed feed = new SyndFeedInput()
					.build(new XmlReader(new URL("https://www.history.com/.rss/full/this-day-in-history")));

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("On This Day");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField(feed.getEntries().get(0).getTitle(), "", false);
			meb.addField("More information can be found", "[here](" + feed.getEntries().get(0).getLink() + ")", false);

			event.getChannel().sendMessage(meb.build()).queue();

		} catch (Exception e) {
		}
	}
}