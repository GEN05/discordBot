package events;

import main.Prefix;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Online extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "онлайн")) {
            int counter = 0;
            for (int i = 0; i < event.getGuild().getMembers().size(); i++) {
                if (event.getGuild().getMembers().get(i).getOnlineStatus() == OnlineStatus.ONLINE || event.getGuild().getMembers().get(i).getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
                    counter++;
                }
            }
            event.getChannel()
                    .sendMessage("Пользователей онлайн: " + counter).queue();
        }
    }
}
