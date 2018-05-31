package me.kavin.mememachine;

import javax.security.auth.login.LoginException;

import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.listener.DiscordListener;
import me.kavin.mememachine.utils.Timer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter{
	public static JDA api;
	public static Timer uptime = new Timer();
	public static String GOOGLE_API_KEY;

	public static void main(String[] args) {
		try {
			GOOGLE_API_KEY = System.getenv().get("GOOGLE_API_KEY");
			JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(System.getenv().get("BOT_TOKEN"));
			api = builder.buildAsync();
			api.setAutoReconnect(true);
			DiscordListener.init();
			uptime.reset();
			new CommandManager();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}