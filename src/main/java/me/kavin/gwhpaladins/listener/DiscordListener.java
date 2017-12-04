package me.kavin.gwhpaladins.listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
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

		List<Guild> guilds = Main.api.getGuilds();
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				int index = 0;

				while(true){
					index++;
					for(Guild g : guilds){
						if(index >= colors.size()){
							index = 0;
						}
						for(Role role : g.getRoles()){
							System.out.println(role.getName());
							if(!role.getName().equalsIgnoreCase("Meme Machine") && !role.getName().equalsIgnoreCase("@everyone") && !role.getName().equalsIgnoreCase("JukeBot") && !role.getName().equalsIgnoreCase("the memer") && !role.getName().equalsIgnoreCase("illegal hacker") && !role.getName().equalsIgnoreCase("THE GODDD!!!!!!!") && !role.getName().equalsIgnoreCase("himebot"))
							role.getManager().setColor(colors.get(index)).queue();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
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