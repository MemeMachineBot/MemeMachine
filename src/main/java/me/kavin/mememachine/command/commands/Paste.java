package me.kavin.mememachine.command.commands;

import org.jsoup.Jsoup;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Paste extends Command {
	public Paste() {
		super(">paste", "`Uploads the given text to Bisoga`");
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

			if (q == null) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Error: No Arguments provided!");
				meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			String url;

			try {
				url = Jsoup.parse(Unirest.post("https://bisoga.glitch.me/paste").field("paste", q).asString().getBody())
						.select("#text > a").text();
			} catch (Exception e) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Bisoga: Bisoga is down!");
				meb.setDescription("If this is an error, report it on the github.");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Bisoga");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField("`Heres your Bisoga Paste link: `", url, true);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}