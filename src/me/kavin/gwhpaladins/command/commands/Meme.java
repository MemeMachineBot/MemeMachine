package me.kavin.gwhpaladins.command.commands;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Meme extends Command{
	public Meme(){
	super(".meme");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		
	}
	}
}
