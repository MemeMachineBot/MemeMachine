package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UpTime extends Command {
	public UpTime() {
		super(">uptime", "`Shows how long the bot has been running for`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		long millis = Main.uptime.getDifference();
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		long days = (millis / (1000 * 60 * 60)) / 24;

		EmbedBuilder meb = new EmbedBuilder();

		meb.setTitle("Uptime");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		meb.addField("Day", String.valueOf(days) + "\n", false);
		meb.addField("Hour", String.valueOf(hour) + "\n", false);
		meb.addField("Minute", String.valueOf(minute) + "\n", false);
		meb.addField("Second", String.valueOf(second) + "\n", false);

		event.getChannel().sendMessage(meb.build()).complete();
	}
}