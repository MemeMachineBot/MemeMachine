package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command {
    public Help() {
        super(">help", "`Shows this message`");
    }
    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.equalsIgnoreCase(getPrefix())) {
            EmbedBuilder meb = new EmbedBuilder();
            meb.setColor(ColorUtils.getRainbowColor(2000));
            meb.setTitle("Meme Machine's Commands:");
            for (Command cmd: CommandManager.commands) {
                meb.addField(cmd.getPrefix(), cmd.getHelp() + '\n', false);
            }
            event.getChannel().sendMessage(meb.build()).complete();
        }
    }
}