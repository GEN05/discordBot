package events;

import main.Prefix;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Game extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "Игра"))
            if (!event.getMember().getUser().isBot())
                if (event.getMember().getGame() == null)
                    event.getChannel()
                            .sendMessage(event.getMember().getEffectiveName()
                                    + " сейчас ни во что не играет")
                            .queue();
                else
                    event.getChannel()
                            .sendMessage(event.getMember().getEffectiveName()
                                    + " играет в "
                                    + event.getMember().getGame().getName())
                            .queue();
    }
}
