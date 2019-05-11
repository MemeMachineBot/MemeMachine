package me.kavin.mememachine.command.commands;

import org.apache.commons.lang3.StringUtils;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BubbleText extends Command {

	public BubbleText() {
		super(">bubbletext", "`Converts your text to Bubble Text!`");
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

			meb.setTitle("Bubble Text: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			String converted = q;

			converted = converted.replace('0', 'ⓞ').replace('1', '➀').replace('2', '➁').replace('3', '➂')
					.replace('4', '➃').replace('5', '➄').replace('6', '➅').replace('7', '➆').replace('8', '➇')
					.replace('9', '➈');

			converted = StringUtils.replaceIgnoreCase(converted, "a", "ⓐ");
			converted = StringUtils.replaceIgnoreCase(converted, "b", "ⓑ");
			converted = StringUtils.replaceIgnoreCase(converted, "c", "ⓒ");
			converted = StringUtils.replaceIgnoreCase(converted, "d", "ⓓ");
			converted = StringUtils.replaceIgnoreCase(converted, "e", "ⓔ");
			converted = StringUtils.replaceIgnoreCase(converted, "f", "ⓕ");
			converted = StringUtils.replaceIgnoreCase(converted, "g", "ⓖ");
			converted = StringUtils.replaceIgnoreCase(converted, "h", "ⓗ");
			converted = StringUtils.replaceIgnoreCase(converted, "i", "ⓘ");
			converted = StringUtils.replaceIgnoreCase(converted, "j", "ⓙ");
			converted = StringUtils.replaceIgnoreCase(converted, "k", "ⓚ");
			converted = StringUtils.replaceIgnoreCase(converted, "l", "ⓛ");
			converted = StringUtils.replaceIgnoreCase(converted, "m", "ⓜ");
			converted = StringUtils.replaceIgnoreCase(converted, "n", "ⓝ");
			converted = StringUtils.replaceIgnoreCase(converted, "o", "ⓞ");
			converted = StringUtils.replaceIgnoreCase(converted, "p", "ⓟ");
			converted = StringUtils.replaceIgnoreCase(converted, "q", "ⓠ");
			converted = StringUtils.replaceIgnoreCase(converted, "r", "ⓡ");
			converted = StringUtils.replaceIgnoreCase(converted, "s", "ⓢ");
			converted = StringUtils.replaceIgnoreCase(converted, "t", "ⓣ");
			converted = StringUtils.replaceIgnoreCase(converted, "u", "ⓤ");
			converted = StringUtils.replaceIgnoreCase(converted, "v", "ⓥ");
			converted = StringUtils.replaceIgnoreCase(converted, "w", "ⓦ");
			converted = StringUtils.replaceIgnoreCase(converted, "x", "ⓧ");
			converted = StringUtils.replaceIgnoreCase(converted, "y", "ⓨ");
			converted = StringUtils.replaceIgnoreCase(converted, "z", "ⓩ");

			meb.setDescription(converted);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}