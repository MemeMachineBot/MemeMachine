package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QRCode extends Command {

    public QRCode() {
        super(">qrcode", "`Shows a QR Code with the given URL`");
    }

    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.toLowerCase().startsWith(getPrefix())) {
        	String[] split = message.split(" ");
        	if (split.length != 1) {
        		event.getChannel().sendMessage(getMessage(split[1])).complete();
            } else {
            	event.getChannel().sendMessage("`Please provide a URL as your argument like` \n>qrcode <URL>").complete();
            }
        }

    }
    private MessageEmbed getMessage(String url) {
        try {
            EmbedBuilder meb = new EmbedBuilder();

            meb.setTitle("QR Code");

            meb.setColor(ColorUtils.getRainbowColor(2000));

            meb.setImage("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + URLEncoder.encode(url, "UTF-8"));

            return meb.build();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}