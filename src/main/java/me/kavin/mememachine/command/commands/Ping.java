package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends Command {
	public Ping() {
		super(">ping", "`Displays the ping to discord servers!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage("The ping took `" + event.getJDA().getGatewayPing() + "` ms!").queue();
	}
}