package events;

import main.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class Help extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Prefix.PREFIX + "помоги")) {
            log(event.getChannel());
        }
    }

    public void log(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Привет!");
        builder.setColor(Color.decode("#0652DD"));
        builder.setDescription("Я могу:");
        builder.addField("Поздороваться", "!Привет", false);
        builder.addField("Посмотреть в какую игру ты играешь", "!игра", false);
        builder.addField("Сказать сколько людей сейчас онлайн", "!онлайн", false);
        builder.addField("Проверить тебя", "!тест", false);
        builder.addField("Надоел человек? Просто введи ", "!mute @никнейм {Причина}", false);
        builder.addField("Удалять сообщения ", "!удали #канал количество", false);
        channel.sendMessage(builder.build()).queue();
    }
}