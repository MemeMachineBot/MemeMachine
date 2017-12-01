package me.kavin.gwhpaladins.listener;

import java.util.ArrayList;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter{
	public static void init(){
	Main.api.addEventListener(new DiscordListener());
	}
	@Override
	public void onReady(ReadyEvent event) {
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help", "Hax.kill"));
		Main.channels = new ArrayList<TextChannel>(Main.api.getTextChannels());
		/*Main.channels.forEach( channel -> {
			channel.sendMessage("Bot by @Kavin#3412 Message For Bug Fixes").queue();
		});*/
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser()){
		return;
	}
	for (Command cmd : CommandManager.commands)
	cmd.onCommand(event.getMessage().getRawContent() , event);
	}
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
	event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").queue();
	}
}