package me.kavin.mememachine.command.commands;

import org.apache.commons.lang3.StringUtils;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UpsideDownText extends Command {

	public UpsideDownText() {
		super(">upsidedowntext", "`Converts your text to UpsideDown Text!`");
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

			meb.setTitle("UpsideDown Text: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			String converted = q;

			converted = StringUtils.reverse(converted.replace('0', '0').replace('1', 'Ɩ').replace('2', 'ᄅ')
					.replace('3', 'Ɛ').replace('4', 'ㄣ').replace('5', 'ϛ').replace('6', '9').replace('7', 'ㄥ')
					.replace('8', '8').replace('9', '6').replace('a', 'ɐ').replace('b', 'B').replace('c', 'ɔ')
					.replace('d', 'p').replace('e', 'ǝ').replace('f', 'ɟ').replace('g', 'ƃ').replace('h', 'ɥ')
					.replace('i', 'ı').replace('j', 'ɾ').replace('k', 'ʞ').replace('l', 'ן').replace('m', 'ɯ')
					.replace('n', 'u').replace('o', 'o').replace('p', 'd').replace('q', 'b').replace('r', 'ɹ')
					.replace('s', 's').replace('t', 'ʇ').replace('u', 'n').replace('v', 'ʌ').replace('w', 'ʍ')
					.replace('x', 'x').replace('y', 'ʎ').replace('z', 'z').replace('A', '∀').replace('B', 'q')
					.replace('C', 'Ͻ').replace('D', 'ᗡ').replace('E', 'Ǝ').replace('F', 'Ⅎ').replace('G', 'ƃ')
					.replace('H', 'H').replace('I', 'I').replace('J', 'ſ').replace('K', 'ʞ').replace('L', '˥')
					.replace('M', 'W').replace('N', 'N').replace('O', 'O').replace('P', 'Ԁ').replace('Q', 'Ὁ')
					.replace('R', 'ᴚ').replace('S', 'S').replace('T', '⊥').replace('U', '∩').replace('V', 'Λ')
					.replace('W', 'M').replace('X', 'X').replace('Y', 'ʎ').replace('Z', 'Z'));

			meb.setDescription(converted);

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}