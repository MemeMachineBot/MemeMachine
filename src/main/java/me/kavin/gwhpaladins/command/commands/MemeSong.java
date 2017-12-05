package me.kavin.gwhpaladins.command.commands;

import java.util.ArrayList;
import java.util.Random;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MemeSong extends Command{

	ArrayList<String> memesongs = new ArrayList<String>();
	VoiceChannel channel = null;

	public MemeSong(){
		super(".memesong");
		memesongs.add("Jake Paul - It's Everyday Bro (Song) feat. Team 10 (Official Music Video)");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		
		event.getChannel().sendMessage("").queue();
	}
	}
	@SuppressWarnings("unused")
	private String getMemeSong() {
		return memesongs.get(new Random().nextInt(memesongs.size()));
	}
}
