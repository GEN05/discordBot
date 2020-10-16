package events;

import main.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Clear extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "удали")) {
            if (args.length <= 2) {
                sendErrorMessage(event.getChannel(), event.getMember());
            } else {
                TextChannel target = event.getMessage().getMentionedChannels().get(0);
                int size = Integer.parseInt(args[2]);
                if (size > 30) {
                    sendErrorMessage(event.getChannel(), event.getMember());
                    return;
                }
                purgeMessages(target, size);
                StringBuilder reason = new StringBuilder();
                if (args.length > 3) {
                    for (int i = 3; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }
                }
                log(event.getMember(), args[2], reason.toString(), event.getGuild().getTextChannelsByName("devs", true).get(0), target);
            }
        }
    }

    public void purgeMessages(TextChannel channel, int num) {
        MessageHistory history = new MessageHistory(channel);
        List<Message> messages;
        messages = history.retrievePast(num).complete();
        channel.deleteMessages(messages).queue();
    }

    public void sendErrorMessage(TextChannel channel, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Ошибка ввода");
        builder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        builder.setColor(Color.decode("#EA2027"));
        builder.addField("Правильное использование: !удали {#канал} {причина}", "", false);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void log(Member clearer, String num, String reason, TextChannel incident, TextChannel cleared) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Отчёт");
        builder.setColor(Color.decode("#0652DD"));
        builder.addField("Какой", cleared.getAsMention(), false);
        builder.addField("Сколько сообщений", num, false);
        tempLog(clearer, reason, incident, sdf, stf, date, builder, "Откуда");
    }

    static void tempLog(Member clearer, String reason, TextChannel incident, SimpleDateFormat sdf,
                        SimpleDateFormat stf, Date date, EmbedBuilder builder, String from) {
        builder.addField(from, clearer.getAsMention(), false);
        builder.addField("Причина", reason, false);
        builder.addField("Дата", sdf.format(date), false);
        builder.addField("Время", stf.format(date), false);
        incident.sendMessage(builder.build()).queue();
    }
}

