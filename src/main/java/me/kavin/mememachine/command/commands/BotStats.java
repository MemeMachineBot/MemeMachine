package me.kavin.mememachine.command.commands;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BotStats extends Command {

	private DecimalFormat df = new DecimalFormat("#.##");

	public BotStats() {
		super(">botstats", "`Shows you the bot's stastics`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())) {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Bot Statistics");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
			long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
			long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
			long usedMemory = totalMemory - freeMemory;
			double cpuUsage = getProcessCpuLoad();

			meb.addField("Users: ", String.valueOf(Main.api.getUsers().size()) + "\n", false);
			meb.addField("Servers: ", String.valueOf(Main.api.getGuilds().size()), false);

			meb.addBlankField(false);

			meb.addField("CPU Usage: ", String.valueOf(cpuUsage) + "\n", false);
			meb.addField("Max Memory: ", String.valueOf(maxMemory) + "\n", false);
			meb.addField("Total Memory: ", String.valueOf(totalMemory) + "\n", false);
			meb.addField("Free Memory: ", String.valueOf(freeMemory) + "\n", false);
			meb.addField("Used Memory: ", String.valueOf(usedMemory) + "\n", false);

			event.getChannel().sendMessage(meb.build()).complete();
		}
	}

	private double getProcessCpuLoad() {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		AttributeList list = null;

		try {
			list = mbs.getAttributes(ObjectName.getInstance("java.lang:type=OperatingSystem"),
					new String[] { "ProcessCpuLoad" });
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (list.isEmpty())
			return Double.NaN;

		Attribute att = (Attribute) list.get(0);
		Double value = (Double) att.getValue();

		if (value == -1.0)
			return Double.NaN;
		return Double.parseDouble(df.format((value * 1000) / 10.0));
	}
}