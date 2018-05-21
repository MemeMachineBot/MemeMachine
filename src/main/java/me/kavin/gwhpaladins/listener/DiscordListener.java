package me.kavin.gwhpaladins.listener;

import java.awt.Color;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;
import me.kavin.gwhpaladins.lists.Api;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {
	
	public static void init(){
	Main.api.addEventListener(new DiscordListener());
	}
	@Override
	public void onReady(ReadyEvent event) {
		Api.loop();
		Main.api.getPresence().setStatus(OnlineStatus.IDLE);
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help | " + Main.api.getUsers().size() + " Users!", "Hax.kill"));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					for(Guild g : Main.api.getGuilds()){
						for(Role role : g.getRoles()){
							if(role.getName().equalsIgnoreCase("rainbow")) {
								role.getManager().setColor(getRainbowColor(120000)).queue();
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) { }
							}
						}
					}
				}
				
			}
		}).start();
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		if (!event.getGuild().getMember(Main.api.getSelfUser()).hasPermission(Permission.ADMINISTRATOR)) {
			event.getGuild().getDefaultChannel().sendMessage("Please ask a server admistrator to invite me!").queue();
		}
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help | " + Main.api.getUsers().size() + " Users!", "Hax.kill"));
	}
	
	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getOwner().getUser().openPrivateChannel().complete().sendMessage("Congrats on becoming the new owner of " + event.getGuild().getName()).queue();
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help | " + Main.api.getUsers().size() + " Users!", "Hax.kill"));
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser() || event.getAuthor().isBot()){
		return;
	}
	for (Command cmd : CommandManager.commands)
		cmd.onCommand(event.getMessage().getContentRaw() , event);
	}
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getContentRaw().startsWith("."))
		event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").queue();
	}
    public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
}