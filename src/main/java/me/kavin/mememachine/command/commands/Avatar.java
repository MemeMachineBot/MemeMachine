package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Avatar extends Command {
	public Avatar() {
		super(">avatar", "`Shows the person you tag's profile picture`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {

		String[] split = message.split(" ");

		if (split.length < 2) {
			event.getChannel().sendMessage("`Please tag a person as your argument like` \n>avatar "
					+ event.getJDA().getSelfUser().getAsMention()).queue();
			return;
		}

		long memberId;

		try {
			memberId = getLong(split[1]);
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage("`Please tag a person as your argument like` \n>avatar "
					+ event.getJDA().getSelfUser().getAsMention()).queue();
			return;
		}

		if (event.getGuild().getMemberById(memberId) != null)
			event.getChannel().sendMessage(event.getGuild().getMemberById(memberId).getUser().getAvatarUrl())
					.queue();
		else
			event.getChannel().sendMessage("`Please tag a person as your argument like` \n>avatar "
					+ event.getJDA().getSelfUser().getAsMention()).queue();
	}

	private long getLong(String s) throws NumberFormatException {
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < s.length(); i++)
			if ((c = s.charAt(i)) >= '0' && c <= '9')
				sb.append(c);
		return Long.parseLong(sb.toString());
	}

}