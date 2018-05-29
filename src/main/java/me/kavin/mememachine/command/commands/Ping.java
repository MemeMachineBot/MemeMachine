package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends Command{
	public Ping(){
	super(">ping", "`Displays the ping to discord servers!`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())){
			event.getChannel().sendMessage("The ping took `" + Main.api.getPing() / 2 + "` ms!").complete();
		}
	}
}
