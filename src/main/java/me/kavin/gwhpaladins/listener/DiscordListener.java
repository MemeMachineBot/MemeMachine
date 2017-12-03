package me.kavin.gwhpaladins.listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
import net.dv8tion.jda.core.Region;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter{
	
	ArrayList<Color> colors = new ArrayList<Color>();
	
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
		
		colors.add(Color.RED);
		colors.add(Color.ORANGE);
		colors.add(Color.BLUE.darker().darker().darker());
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<Guild> guilds = Main.api.getGuilds();
				int index = 0;

				while(true){
					for(Guild g : guilds){
						index++;
						if(index >= colors.size()){
							index = 0;
						}
						if(!g.getMembersByName("Kavin", true).isEmpty())
						if (g.getMembersByName("Kavin", true).get(0).getRoles().size() == 1)
						for(Role role : g.getMembersByName("Kavin", true).get(0).getRoles()){
							role.getManager().setColor(colors.get(index)).queue();
							try {
								Thread.sleep(750);
							} catch (Exception e) {
								
							}
						}
						if(!g.getMembersByName("pradetor", true).isEmpty())
						for(Role role : g.getMembersByName("pradetor", true).get(0).getRoles()){
							role.getManager().setColor(colors.get(index)).queue();
							try {
								Thread.sleep(750);
							} catch (Exception e) {
								
							}
						}
					}
				}
				
			}
		}).start();
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