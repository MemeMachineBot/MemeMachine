package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Profile extends Command{
	public Profile(){
	super(">profile `Shows the person you tag's profile picture`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (event.isFromType(ChannelType.PRIVATE)){
	return;
	}
	if (message.toLowerCase().startsWith(">profile")){
		
		String[] split = message.split(" ");
		
		if (split.length < 2) {
			event.getChannel().sendMessage("`Please tag a person as your argument like` \n>profile " + Main.api.getSelfUser().getAsMention()).queue();
			return;
		}

		long memberId;
		
		try {
			memberId = getLong(split[1]);
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage("`Please tag a person as your argument like` \n>profile " + Main.api.getSelfUser().getAsMention()).queue();
			return;
		}

		if (event.getGuild().getMemberById(memberId) != null)
			event.getChannel().sendMessage(event.getGuild().getMemberById(memberId).getUser().getAvatarUrl()).queue();
	}
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
