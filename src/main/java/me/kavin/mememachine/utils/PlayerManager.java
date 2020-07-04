package me.kavin.mememachine.utils;

import org.apache.commons.lang3.NotImplementedException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {

	private static PlayerManager INSTANCE;
	private final AudioPlayerManager playerManager;
	private final Long2ObjectOpenHashMap<GuildMusicManager> musicManagers;

	private PlayerManager() {

		this.musicManagers = new Long2ObjectOpenHashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {

		long guildId = guild.getIdLong();
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager, guildId);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	public void loadAndPlay(TextChannel channel, String trackUrl) {

		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				play(musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				playlist.getTracks().forEach(track -> play(musicManager, track));
			}

			@Override
			public void noMatches() {
				throw new NotImplementedException("noMatches");
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				exception.printStackTrace();
			}

		});

	}

	private void play(GuildMusicManager musicManager, AudioTrack track) {
		musicManager.scheduler.queue(track);
	}

	public static synchronized PlayerManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}

		return INSTANCE;
	}
}