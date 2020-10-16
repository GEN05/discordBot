package events;

import main.Prefix;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Hello extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "Привет"))
            if (!event.getMember().getUser().isBot())
                event.getChannel()
                        .sendMessage("Привет "
                                + event.getMember().getEffectiveName()
                                + "!")
                        .queue();
    }
}
