package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerStats extends Command {

	public ServerStats() {
		super(">serverstats", "`Shows you the server stastics`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		EmbedBuilder meb = new EmbedBuilder();

		meb.setTitle("Server Statistics");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		int bots = 0;
		int users = 0;
		int online = 0;

		for (Member member : event.getGuild().getMembers()) {
			if (member.getUser().isBot())
				bots++;
			else {
				users++;
				if (member.getOnlineStatus() != OnlineStatus.OFFLINE)
					online++;
			}
		}

		meb.addField("Bots: ", String.valueOf(bots) + "\n", false);
		meb.addField("Users: ", String.valueOf(users) + "\n", false);
		meb.addField("Online Users: ", String.valueOf(online) + "\n", false);

		event.getChannel().sendMessage(meb.build()).queue();
	}
}