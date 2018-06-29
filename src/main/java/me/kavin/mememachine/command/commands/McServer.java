package me.kavin.mememachine.command.commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class McServer extends Command {

	public McServer() {
		super(">mcserver", "`Searches for a minecraft server`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			String q = null;

			for (int i = getPrefix().length() + 1; i < message.length(); i++) {
				if (q == null)
					q = "";
				q += message.charAt(i);
			}

			if (q != null) {
				EmbedBuilder meb = new EmbedBuilder();

				meb.setTitle("Minecraft Server Search");
				meb.setColor(ColorUtils.getRainbowColor(2000));

				Document doc = Jsoup.parse(
						Unirest.post("https://minecraftservers.org/search").field("term", q).asString().getBody());

				Elements names = doc.select(
						"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-name > h3 > a");
				Elements ips = doc.select(
						"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-server > div > a");
				Elements players = doc.select(
						"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-players > span");
				Elements online = doc.select(
						"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-status > span");
				Elements ranking = doc.select(
						"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-rank > span");

				if (names.size() > 0) {
					meb.addField("Server Name: ", names.get(0).text() + "\n", false);
					meb.addField("Server IP: ", ips.get(0).attr("data-clipboard-text") + "\n", false);
					meb.addField("Players: ", players.get(0).text() + "\n", false);
					meb.addField("Online Status: ", online.get(0).text() + "\n", false);
					meb.addField("Ranking: ", ranking.get(0).text() + "\n", false);
					meb.setImage("https://minecraftservers.org" + doc.selectFirst(
							"#main > div.sponsored-servers.container.cf > table > tbody > tr:nth-child(1) > td.col-server > a > img")
							.attr("src"));
				} else {
					meb.setDescription("No servers found!");
				}

				event.getChannel().sendMessage(meb.build()).complete();
			} else {
				event.getChannel()
						.sendMessage("`Please provide an server name as your argument like` \n>mcserver <name>")
						.complete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}