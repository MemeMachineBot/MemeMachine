package me.kavin.gwhpaladins.listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter{
	
	HashMap<String, String> lastMsg = new HashMap<String, String>();
	
	public static void init(){
	Main.api.addEventListener(new DiscordListener());
	}
	@Override
	public void onReady(ReadyEvent event) {
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help", "Hax.kill"));
		Main.channels = new ArrayList<TextChannel>(Main.api.getTextChannels());
		
		List<Guild> guilds = Main.api.getGuilds();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					for(Guild g : guilds){
						for(Role role : g.getRoles()){
							if(role.getName().equalsIgnoreCase("rainbow"))
							role.getManager().setColor(getRainbowColor(60000)).queue();
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							}
						}
					}
				}
				
			}
		}).start();
	}
	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getTextChannels().forEach( tc -> {
			
		});
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	if(event.getAuthor().isBot() || event.getMessage().getRawContent().startsWith(".") || event.getMessage().getRawContent().startsWith("t!") || event.getMessage().getRawContent().startsWith("`")){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(120000);
					event.getMessage().delete().queue();
				} catch (InterruptedException e) {
				}
				event.getMessage().delete().queue();
			}
		}).start();
	} else {
		if(!event.getAuthor().isBot() && lastMsg.containsKey(event.getAuthor().getName())) {
			if(lastMsg.get(event.getAuthor().getName()).startsWith(event.getMessage().getRawContent()) && event.getMessage().getAttachments().isEmpty()) {
				event.getChannel().sendMessage("You may not spam!").queue();
				event.getMessage().delete().queue();
			}
		}
		lastMsg.put(event.getAuthor().getName(), event.getMessage().getRawContent());
	}
	if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser()){
		return;
	}
	for (Command cmd : CommandManager.commands)
		cmd.onCommand(event.getMessage().getRawContent() , event);
	}
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getRawContent().startsWith("."))
		event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").queue();
	}
    public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
}