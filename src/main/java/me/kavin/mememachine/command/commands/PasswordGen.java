package me.kavin.mememachine.command.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PasswordGen extends Command {
	
	List<Integer> l = new ArrayList<>();
	
    public PasswordGen() {
        super(">password", "`Gives you a random secure password to use!`");
        for (int i = 33; i < 127; i++) {
            l.add(i);
        }
        
        l.remove(new Integer(34));
        l.remove(new Integer(47));
        l.remove(new Integer(92));
        
    }
    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.equalsIgnoreCase(getPrefix())) {
        	
        	StringBuilder sb = new StringBuilder();
        	
            for (int i = 0; i < 10; i++) {
                sb.append((char) l.get(ThreadLocalRandom.current().nextInt(l.size())).intValue());
            }

            event.getAuthor().openPrivateChannel().complete().sendMessage("Heres a random password:\n`" + sb.toString() + "`").complete();
        }
    }
}