package me.kavin.mememachine.utils;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class AudioConnectionListener implements ConnectionListener {

	private static final Long2LongOpenHashMap pings = new Long2LongOpenHashMap();

	private long id;

	public AudioConnectionListener(Guild guild) {
		id = guild.getIdLong();
	}

	@Override
	public void onPing(long ping) {
		pings.put(id, ping);
	}

	@Override
	public void onStatusChange(ConnectionStatus status) {
		// do nothing
	}

	@Override
	public void onUserSpeaking(User user, boolean speaking) {
		// do nothing
	}

	public static long getPing(Guild guild) {
		return pings.get(guild.getIdLong());
	}

	public static boolean hasPing(Guild guild) {
		return pings.containsKey(guild.getIdLong());
	}
}
