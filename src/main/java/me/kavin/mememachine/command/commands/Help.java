package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command{
	public Help(){
	super(">help `Shows this message`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(">help")){
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
