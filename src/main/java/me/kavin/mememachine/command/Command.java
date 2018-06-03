package me.kavin.mememachine.command;

import me.kavin.mememachine.event.EventManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Command {

	private String help;
	private String prefix;

	public Command(String prefix, String help) {
		this.prefix = prefix;
		this.help = help;
		EventManager.getDefault().register(this);
	}

	public String getHelp() {
		return help;
	}

	public String getPrefix() {
		return prefix;
	}

	public void onCommand(String string, MessageReceivedEvent event) {

	}
}