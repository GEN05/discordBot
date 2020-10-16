package events;

import main.Prefix;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Random;

public class Gey extends ListenerAdapter {
    public static int generateRandomInt(int upperRange) {
        Random random = new Random();
        return random.nextInt(upperRange);
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "тест"))
            if (!event.getMember().getUser().isBot())
                event.getChannel()
                        .sendMessage(event.getMember().getEffectiveName()
                                + " пидор на "
                                + generateRandomInt(100)
                                + "%")
                        .queue();
    }
}
