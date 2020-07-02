package me.kavin.mememachine;

import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.listener.DiscordListener;
import me.kavin.mememachine.lists.API;
import me.kavin.mememachine.utils.Timer;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main extends ListenerAdapter {

	public static ShardManager api;
	public static Timer uptime = new Timer();

	public static void main(String[] args) {
		try {
			new CommandManager();
			DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Constants.BOT_TOKEN);
			builder.addEventListeners(new DiscordListener());
			builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS);
			builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));
			builder.setChunkingFilter(ChunkingFilter.NONE);
			builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
			builder.setLargeThreshold(50);
			builder.setCompression(Compression.ZLIB);
			builder.setAutoReconnect(true);
			api = builder.build();
			API.loop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}