package me.kavin.mememachine;

import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.listener.DiscordListener;
import me.kavin.mememachine.utils.Timer;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main extends ListenerAdapter {

	public static ShardManager api;
	public static Timer uptime = new Timer();

	public static void main(String[] args) {
		try {
			DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder(Constants.BOT_TOKEN);
			builder.setAutoReconnect(true);
			builder.addEventListeners(new DiscordListener());
			api = builder.build();
			new CommandManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}