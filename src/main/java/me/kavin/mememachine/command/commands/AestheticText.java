package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AestheticText extends Command {

	public AestheticText() {
		super(">aesthetictext", "`Converts your text to Aesthetic Text!`");
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

			meb.setTitle("Aesthetic Text: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			String converted = q;

			converted = converted.replace('0', '０').replace('1', '１').replace('2', '２').replace('3', '３')
					.replace('4', '４').replace('5', '５').replace('6', '６').replace('7', '７').replace('8', '８')
					.replace('9', '９').replace('a', 'ａ').replace('b', 'ｂ').replace('c', 'ｃ').replace('d', 'ｄ')
					.replace('e', 'ｅ').replace('f', 'ｆ').replace('g', 'ｇ').replace('h', 'ｈ').replace('i', 'ｉ')
					.replace('j', 'ｊ').replace('k', 'ｋ').replace('l', 'ｌ').replace('m', 'ｍ').replace('n', 'ｎ')
					.replace('o', 'ｏ').replace('p', 'ｐ').replace('q', 'ｑ').replace('r', 'ｒ').replace('s', 'ｓ')
					.replace('t', 'ｔ').replace('u', 'ｕ').replace('v', 'ｖ').replace('w', 'ｗ').replace('x', 'ｘ')
					.replace('y', 'ｙ').replace('z', 'ｚ').replace('A', 'Ａ').replace('B', 'Ｂ').replace('C', 'Ｃ')
					.replace('D', 'Ｄ').replace('E', 'Ｅ').replace('F', 'Ｆ').replace('G', 'Ｇ').replace('H', 'Ｈ')
					.replace('I', 'Ｉ').replace('J', 'Ｊ').replace('K', 'Ｋ').replace('L', 'Ｌ').replace('M', 'Ｍ')
					.replace('N', 'Ｎ').replace('O', 'Ｏ').replace('P', 'Ｐ').replace('Q', 'Ｑ').replace('R', 'Ｒ')
					.replace('S', 'Ｓ').replace('T', 'Ｔ').replace('U', 'Ｕ').replace('V', 'Ｖ').replace('W', 'Ｗ')
					.replace('X', 'Ｘ').replace('Y', 'Ｙ').replace('Z', 'Ｚ').replace(' ', '　');

			meb.setDescription(converted);

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}