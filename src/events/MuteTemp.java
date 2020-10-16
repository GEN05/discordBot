package events;

import main.Prefix;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MuteTemp extends ListenerAdapter {

    int counter = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "mutetemp")) {
            if (args.length <= 2) {
                sendErrorMessage(event.getChannel(), event.getMember());
            } else {
                Member target = event.getMessage().getMentionedMembers().get(0);
                tempmute(target, parseTimeAmount(args[2]), parseTimeUnit(args[2]));
                StringBuilder reason = new StringBuilder();
                if (args.length >= 4) {
                    for (int i = 3; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }
                }
                log(target, event.getMember(), reason.toString(), event.getGuild().getTextChannelsByName("devs", true).get(0));
            }
        }
    }

    private int parseTimeAmount(String time) {
        char[] t = time.toCharArray();
        int breakPoint = 0;
        StringBuilder amount = new StringBuilder();
        int parseAmount;
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 's' || t[i] == 'S') {
                breakPoint = i;
                break;
            } else if (t[i] == 'm' || t[i] == 'M') {
                breakPoint = i;
                break;
            }
        }
        for (int i = 0; i < breakPoint; i++)
            amount.append(t[i]);
        parseAmount = Integer.parseInt(amount.toString());
        return parseAmount;
    }

    private TimeUnit parseTimeUnit(String time) {
        TimeUnit unit = TimeUnit.MILLISECONDS;
        char[] t = time.toCharArray();
        for (char c : t) {
            if (c == 's' || c == 'S') {
                unit = TimeUnit.SECONDS;
                break;
            } else if (c == 'm' || c == 'M') {
                unit = TimeUnit.MINUTES;
                break;
            }
        }
        return unit;
    }

    public void tempmute(Member target, int time, TimeUnit unit) {
        Role muted = target.getGuild().getRolesByName("Muted", true).get(0);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                target.getGuild().getController().addRolesToMember(target, muted).queue();
                counter++;
                if (counter % 2 == 0) {
                    target.getGuild().getController().removeSingleRoleFromMember(target, muted).queue();
                    this.cancel();
                }
            }
        };
        switch (unit) {
            case SECONDS:
                timer.schedule(task, 0, time * 1000);
                break;
            case MINUTES:
                timer.schedule(task, 0, time * 1000 * 60);
                break;
            case HOURS:
                timer.schedule(task, 0, time * 1000 * 60 * 60);
                break;
            case DAYS:
                timer.schedule(task, 0, time * 1000 * 60 * 60 * 24);
                break;
        }
    }

    public void sendErrorMessage(TextChannel channel, Member member) {
        Mute.logErrorMessage(channel, member, "Правильное использование: !mute {@никнейм} {время, формат : 12H} {причина}");
    }

    public void log(Member muted, Member muter, String reason, TextChannel channel) {
        Mute.logTwo(muted, muter, reason, channel);
    }
}
