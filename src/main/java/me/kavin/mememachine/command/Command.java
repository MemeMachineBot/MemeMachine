package me.kavin.mememachine.command;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.event.EventManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Command {

	private String help;
	private String prefix;
	private String[] aliases;
	private List<String> possiblePrefixes = new ObjectArrayList<>();

	public Command(String prefix, String help) {
		this(prefix, help, null);
	}

	public Command(String prefix, String help, String[] aliases) {

		this.prefix = prefix;
		this.aliases = aliases;

		this.help = help;

		possiblePrefixes.add(prefix);
		if (aliases != null)
			for (String alias : aliases) {
				possiblePrefixes.add(alias);
			}

		EventManager.getDefault().register(this);
	}

	public String getHelp() {
		return help;
	}

	public String getPrefix() {
		return prefix;
	}

	public String[] getAliases() {
		return aliases;
	}

	public List<String> getPossiblePrefixes() {
		return possiblePrefixes;
	}

	public String getQuery(String message) {

		String q = null;

		if (message.contains(" ")) {
			for (String prefix : getPossiblePrefixes()) {
				if (message.length() > prefix.length() && message.startsWith(prefix + " ")) {
					q = "";
					for (int i = prefix.length() + 1; i < message.length(); i++)
						q += message.charAt(i);
				}
			}
		}

		return q;
	}

	public void onCommand(String string, MessageReceivedEvent event) throws Exception {

	}
}