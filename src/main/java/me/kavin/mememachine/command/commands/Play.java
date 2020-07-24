package me.kavin.mememachine.command.commands;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItem.InfoType;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.search.SearchInfo;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.AudioConnectionListener;
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
				} else {
					event.getGuild().getAudioManager().openAudioConnection(vs.getChannel());
					event.getGuild().getAudioManager()
							.setConnectionListener(new AudioConnectionListener(event.getGuild()));
				}
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

		SearchInfo si = getSearchInfo(0, q, Collections.emptyList(), null);

		List<InfoItem> items = si.getRelatedItems();

		InfoItem selected = null;

		{
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("YouTube Search");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			for (int i = 0; i < items.size(); i++) {
				InfoItem item = items.get(i);
				if (item.getInfoType() == InfoType.STREAM || item.getInfoType() == InfoType.PLAYLIST) {
					selected = item;
					break;
				}
			}

			if (selected == null) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			meb.setDescription("`Added to the queue:` [" + selected.getName() + "]" + "(" + selected.getUrl() + ")");

			String thumbnail = selected.getThumbnailUrl();

			if (thumbnail.contains("?"))
				thumbnail = thumbnail.substring(0, thumbnail.indexOf('?'));

			meb.setImage(Constants.IMAGE_PROXY_URL + URLEncoder.encode(thumbnail, "UTF-8"));

			event.getChannel().sendMessage(meb.build()).queue();
		}

		String streaming_url = selected.getUrl();

		PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), streaming_url);
	}

	public SearchInfo getSearchInfo(int serviceId, String searchString, List<String> contentFilters, String sortFilter)
			throws ParsingException, ExtractionException, IOException {
		StreamingService service = NewPipe.getService(serviceId);
		SearchInfo info = SearchInfo.getInfo(service,
				service.getSearchQHFactory().fromQuery(searchString, contentFilters, sortFilter));
		return info;
	}
}