package me.kavin.mememachine.command.commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TopSongs extends Command {

	public TopSongs() {
		super(">topsongs", "`Lists the top songs!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Top Songs");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			Document document = Jsoup
					.parse(Unirest.get("https://www.billboard.com/charts/hot-100").asString().getBody());

			Elements songNames = document.getElementsByClass("chart-row__song");
			Elements songArtists = document.getElementsByClass("chart-row__artist");

			for (int i = 0; i < 5; i++)
				meb.addField("`" + songNames.get(i).text() + "`", songArtists.get(i).text() + "\n", false);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}