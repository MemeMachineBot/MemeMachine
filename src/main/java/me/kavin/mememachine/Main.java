package me.kavin.mememachine;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.listener.DiscordListener;
import me.kavin.mememachine.utils.Timer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	public static JDA api;
	public static Timer uptime = new Timer();

	public static void main(String[] args) {
		try {
			JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Constants.BOT_TOKEN);
			api = builder.buildAsync();
			api.setAutoReconnect(true);
			DiscordListener.init();
			uptime.reset();
			HttpClient customClient = HttpClients.custom()
					.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
					.build();
			Unirest.setHttpClient(customClient);
			new CommandManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}