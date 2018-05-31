package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Cat extends Command {

    public Cat() {
        super(">cat", "`Shows a cat image`");
    }

    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.equalsIgnoreCase(getPrefix())) {
            event.getChannel().sendMessage(getCat()).complete();
        }

    }
    private MessageEmbed getCat() {
        try {
            EmbedBuilder meb = new EmbedBuilder();

            meb.setTitle("Cat");

            meb.setColor(ColorUtils.getRainbowColor(2000));

            meb.setImage("https://cataas.com/cat");

            return meb.build();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}