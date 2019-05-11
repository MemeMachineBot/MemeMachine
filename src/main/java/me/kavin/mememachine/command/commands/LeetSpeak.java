package me.kavin.mememachine.command.commands;

import org.apache.commons.lang3.StringUtils;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LeetSpeak extends Command {

	public LeetSpeak() {
		super(">leetspeak", "`Converts your text to l337 5p33k!`");
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
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("l337 5p33k: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			String converted = q;

			converted = StringUtils.replaceIgnoreCase(converted, "o", "0");
			converted = StringUtils.replaceIgnoreCase(converted, "l", "1");
			converted = StringUtils.replaceIgnoreCase(converted, "i", "1");
			converted = StringUtils.replaceIgnoreCase(converted, "s", "0");
			converted = StringUtils.replaceIgnoreCase(converted, "e", "3");
			converted = StringUtils.replaceIgnoreCase(converted, "a", "4");
			converted = StringUtils.replaceIgnoreCase(converted, "t", "7");

			meb.setDescription(converted);

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}