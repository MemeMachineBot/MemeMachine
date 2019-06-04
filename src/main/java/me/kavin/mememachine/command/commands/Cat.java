package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cat extends Command {

	public Cat() {
		super(">cat", "`Shows a cat image`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage(getCat()).queue();
	}

	private MessageEmbed getCat() {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Cat");

			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setImage("https://cataas.com/cat");

			return meb.build();
		} catch (Throwable t) {
		}
		return null;
	}
}