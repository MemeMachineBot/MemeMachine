package me.kavin.mememachine.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Command {
	private String prefix;
	public Command(String prefix){
	this.prefix = prefix;
	}
	public String getPrefix() {
		return prefix;
	}
	public void onCommand(String string, MessageReceivedEvent event) {
	
	}
}
