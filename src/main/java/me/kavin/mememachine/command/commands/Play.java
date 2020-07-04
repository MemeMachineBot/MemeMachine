package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Play extends Command {

	public Play() {
		super(">play", "`Allows you to play music from youtube.`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {

		if (!event.getMessage().isSuppressedEmbeds())
			event.getMessage().suppressEmbeds(true).queue();

		if (!event.getGuild().getAudioManager().isConnected()) {
			GuildVoiceState vs = event.getMember().getVoiceState();
			if (event.getMember() != null && vs.inVoiceChannel()) {
				Member selfMember = event.getGuild().getSelfMember();
				if (!selfMember.hasPermission(vs.getChannel(), Permission.VOICE_CONNECT)) {
					EmbedBuilder meb = new EmbedBuilder();
					meb.setTitle("Play: Join VC");
					meb.setColor(ColorUtils.getRainbowColor(2000));
					meb.setDescription(String.format("I cannot join %s as I do not have permissions to do so.",
							vs.getChannel().getName()));
					event.getChannel().sendMessage(meb.build()).queue();
					return;
				} else
					event.getGuild().getAudioManager().openAudioConnection(vs.getChannel());
			} else {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setTitle("Play: Join VC");
				meb.setColor(ColorUtils.getRainbowColor(2000));
				meb.setDescription("You are not in a VC.");
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}
		}

		String q = null;

		if (message.length() > getPrefix().length()) {
			q = "";
			for (int i = getPrefix().length() + 1; i < message.length(); i++)
				q += message.charAt(i);
		}

		if (q == null) {
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.setTitle("Error: No Arguments provided!");
			meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
			event.getChannel().sendMessage(meb.build()).queue();
			return;
		}

		String url = "https://invidious.snopyta.org/api/v1/search?type=all&fields=type,title,videoId,videoThumbnails,playlistId,playlistThumbnail&q="
				+ URLEncoder.encode(q, "UTF-8");

		JSONArray jArray = Unirest.get(url).asJson().getBody().getArray();

		JSONObject selected = null;

		{
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("YouTube Search");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			String type = null;

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jObject = jArray.getJSONObject(i);
				type = jObject.getString("type");
				if (type.equals("playlist") || type.equals("video")) {
					selected = jObject;
					break;
				}
			}

			if (selected == null) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			meb.setDescription("`Added to the queue:` [" + selected.getString("title") + "]" + "("
					+ "https://www.youtube.com/watch?v="
					+ (selected.getString("type").equals("video") ? selected.getString("videoId")
							: selected.getString("playlistId"))
					+ ")");

			if (type.equals("video")) {
				JSONArray thumbnails = selected.getJSONArray("videoThumbnails");

				for (int i = 0; i < thumbnails.length(); i++) {
					JSONObject thumbnail = thumbnails.getJSONObject(i);
					String thumbnail_url = thumbnail.getString("url").replace("invidious.snopyta.org", "i.ytimg.com");
					if (Unirest.get(thumbnail_url).asEmpty().isSuccess()) {
						meb.setImage(Constants.IMAGE_PROXY_URL + URLEncoder.encode(thumbnail_url, "UTF-8"));
						break;
					}
				}
			} else
				meb.setImage(Constants.IMAGE_PROXY_URL
						+ URLEncoder.encode(selected.getString("playlistThumbnail"), "UTF-8"));

			event.getChannel().sendMessage(meb.build()).queue();
		}

		String streaming_url = null;

		if (selected.getString("type").equals("video"))
			streaming_url = "https://www.youtube.com/watch?v=" + selected.getString("videoId");
		else
			streaming_url = "https://www.youtube.com/playlist?list=" + selected.getString("playlistId");

		PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), streaming_url);
	}
}