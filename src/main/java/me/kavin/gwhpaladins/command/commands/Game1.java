package me.kavin.gwhpaladins.command.commands;

import java.util.ArrayList;
import java.util.Random;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Game1 extends Command{

	ArrayList<String> pics = new ArrayList<String>();
	static String id;
	int smash = 0;
	int pass = 0;

	public Game1(){
		super(".game 1");
		pics.add("http://ibb.co/cznwTH");
		pics.add("http://ibb.co/imVx2c");
		pics.add("http://ibb.co/mZ7MTH");
		pics.add("http://ibb.co/dj2o8H");
		pics.add("http://ibb.co/ncCYax");
		Main.api.addEventListener(new ListenerAdapter() {
			@Override
			public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
				if(event.getUser().equals(Main.api.getSelfUser())  || (!event.getMessageId().equals(id)))
					return;
				if(event.getMessageId().equals(id)) {
					if(event.getReaction().getReactionEmote().getName().equals("smash"))
						smash++;
					else if (event.getReaction().getReactionEmote().getName().equals("smash")) {
						pass++;
					}
				}
			}
			@Override
			public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
				if(event.getUser().equals(Main.api.getSelfUser()) || (!event.getMessageId().equals(id)))
					return;
				if(event.getMessageId().equals(id)) {
					if(event.getReaction().getReactionEmote().getName().equals("smash"))
						smash--;
					else if (event.getReaction().getReactionEmote().getName().equals("smash")) {
						pass--;
					}
				}
			}
			@Override
			public void onMessageDelete(MessageDeleteEvent event) {
				if(event.getMessageId().equals(id)) {
					id = null;
					if(smash != pass) {
						event.getChannel().sendMessage("The winner was " + (smash > pass ? "Smash with " + smash + " votes!" : "Pass with " + pass + " votes!")).queue();
					} else {
						event.getChannel().sendMessage("There was no winner, it was a tie").queue();
					}
				}
			}
			@Override
			public void onMessageReceived(MessageReceivedEvent event) {
				if(event.getMessage().getRawContent().startsWith("Game 1")) {
					id = event.getMessageId();
					event.getMessage().addReaction(event.getGuild().getEmoteById(418363481142722560L)).queue();
					event.getMessage().addReaction(event.getGuild().getEmoteById(418363700538507265L)).queue();
					smash = 0;
					pass = 0;
				}
			}
		});
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		event.getChannel().deleteMessageById(event.getMessageId()).queue();
		if(id == null) {
			event.getChannel().sendMessage("Game 1: \nSmash Or Pass?\n" + getPic()).queue();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) { }
					event.getMessage().delete().queue();
				}
			}).start();
		}
	}
	}
	private String getPic() {
		return pics.get(new Random().nextInt(pics.size()));
	}
}
