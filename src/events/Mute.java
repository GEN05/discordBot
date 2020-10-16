package events;

import main.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Mute extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "mute")) {
            if (args.length <= 1) {
                sendErrorMessage(event.getChannel(), event.getMember());
            } else {
                Member target = event.getMessage().getMentionedMembers().get(0);
                Role muted = event.getGuild().getRolesByName("Muted", true).get(0);

                event.getGuild().getController().addRolesToMember(target, muted).queue();

                StringBuilder reason = new StringBuilder();
                if (args.length >= 3) {
                    for (int i = 2; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }
                }
                log(target, event.getMember(), reason.toString(), event.getGuild().getTextChannelsByName("devs", true).get(0));
                event.getChannel().getMessageById(event.getChannel().getLatestMessageId()).complete().delete().queue();
            }
        }
    }

    public void sendErrorMessage(TextChannel channel, Member member) {
        logErrorMessage(channel, member, "Правильное использование: !mute {@никнейм} {причина}");
    }

    static void logErrorMessage(TextChannel channel, Member member, String s) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Ошибка ввода");
        builder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        builder.setColor(Color.decode("#EA2027"));
        builder.setDescription("{} = Required, [] = Optional");
        builder.addField(s, "", false);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void log(Member muted, Member muter, String reason, TextChannel channel) {
        logTwo(muted, muter, reason, channel);
    }

    static void logTwo(Member muted, Member muter, String reason, TextChannel channel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Отчёт");
        builder.setColor(Color.decode("#0652DD"));
        builder.addField("Кого", muted.getAsMention(), false);
        Clear.tempLog(muter, reason, channel, sdf, stf, date, builder, "Кто");
    }
}
