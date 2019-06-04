package me.kavin.mememachine.command.commands;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Dab extends Command {
	public Dab() {
		super(">dab", "`Shows a person dabbing`");
		daburls.add("https://ibb.co/kjGceb");
		daburls.add("https://ibb.co/cPbokG");
		daburls.add("https://ibb.co/gX8qzb");
		daburls.add("https://ibb.co/cRzKsw");
		daburls.add("https://ibb.co/ejvEQG");
		daburls.add("https://ibb.co/n6E8kG");
		daburls.add("https://ibb.co/dJgAzb");
		daburls.add("https://ibb.co/gSV3Kb");
		daburls.add("https://ibb.co/cN8qzb");
		daburls.add("https://ibb.co/ndSTkG");
	}

	ArrayList<String> daburls = new ArrayList<String>();

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Dab");
			meb.setImage(getDab());
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setImage(Jsoup.parse(Unirest.get(getDab()).asString().getBody())
					.selectFirst("#image-viewer-container > img").attr("src"));

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}

	private String getDab() {
		return daburls.get(ThreadLocalRandom.current().nextInt(daburls.size()));
	}
}