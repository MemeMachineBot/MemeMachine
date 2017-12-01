package me.kavin.gwhpaladins.command.commands;

import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command{
	public Help(){
	super(".help");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		String send = null;
		for(Command cmd : CommandManager.commands){
		if(send == null){
			send = cmd.getPrefix();
		} else {
			send += '\n' + cmd.getPrefix();
		}
		}
	}
	}
}
