package me.kavin.gwhpaladins.command.commands;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UpTime extends Command{
	public UpTime(){
	super(">uptime `Shows how long the bot has been running for`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (event.isFromType(ChannelType.PRIVATE)){
	return;
	}
	if (message.equalsIgnoreCase(">uptime")){
		long millis = Main.uptime.getDifference();
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		long milliseconds = millis % 1000;
		String time = "Uptime: " + hour + " hour(s) " + minute + " minute(s) " + second + " second(s) " + milliseconds + " milisecond(s)" ;
	event.getChannel().sendMessage(time).queue();
	}
	}
}
