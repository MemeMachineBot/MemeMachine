package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.XpHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Profile extends Command {

	private XpHelper xpHelper = new XpHelper();

	public Profile() {
		super(">profile", "`Displays your level and xp`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Your Profile");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			int xp = xpHelper.getXp(event.getAuthor().getIdLong());

			meb.addField("XP required:", 500 - (xp % 500) + "\n", false);
			meb.addField("Your Level:", xp / 500 + "\n", false);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}