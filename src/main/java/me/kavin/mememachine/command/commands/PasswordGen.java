package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PasswordGen extends Command {

	IntArrayList l = new IntArrayList();

	public PasswordGen() {
		super(">password", "`Gives you a random secure password to use!`");

		for (int i = 33; i < 127; i++) {
			l.add(i);
		}

		l.rem(34);
		l.rem(47);
		l.rem(92);

	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			sb.append((char) l.getInt(ThreadLocalRandom.current().nextInt(l.size())));
		}

		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setTitle("Password: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setDescription("Heres a random password:\n`" + sb.toString() + "`");
			event.getAuthor().openPrivateChannel().complete().sendMessage(meb.build()).queue();
		}
		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setTitle("Password: ");
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setDescription("Check your DM's, if you have them disabled, you can't use this command!");
			event.getChannel().sendMessage(meb.build()).queue();
		}
	}
}