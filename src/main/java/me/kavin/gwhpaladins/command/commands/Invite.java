package me.kavin.gwhpaladins.command.commands;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Invite extends Command{
	public Invite(){
	super(">invite `Gives you a bot invite link`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (event.isFromType(ChannelType.PRIVATE)){
	return;
	}
	if (message.equalsIgnoreCase(">invite")){
		event.getAuthor().openPrivateChannel().complete().sendMessage("`You can invite me here:`\nhttps://discordapp.com/oauth2/authorize?client_id=" + Main.api.getSelfUser().getId() + "&permissions=8&scope=bot").queue();
	}
	}
}
