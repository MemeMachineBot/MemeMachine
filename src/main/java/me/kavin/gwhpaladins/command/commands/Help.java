package me.kavin.gwhpaladins.command.commands;

import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command{
	public Help(){
	super(".help `Shows this message`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(".help")){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		String send = "";
		boolean start = true;
		for(Command cmd : CommandManager.commands){
			if(start){
				send = cmd.getPrefix();
				start = false;
			} else {
				send = send + "\n" + cmd.getPrefix();
			}
		}
		event.getChannel().sendMessage(send).queue();
		}
	}
}
