package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.INFO;

public class StatusCommand extends BaseCommand {
    public StatusCommand() {
        super(INFO, "`status` - Displays info about AkiBot.", "`status`: Displays " +
                "info about AkiBot's current status.", "Status");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));

        if (args.length == 0) {
            embedOutput(event);
        } else {
            event.getChannel().sendMessage("Invalid format. Type `-ab status`.").queue();
        }
    }

    private void embedOutput(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = fillEmbed(new EmbedBuilder(), event);

        long currentUtcTime = System.currentTimeMillis() + 14400000, elapsedTime = currentUtcTime - Main.startupTime;
        String hours = Long.toString(elapsedTime / 3600000), minutes = Long.toString((elapsedTime / 60000) % 60), seconds = Long.toString((elapsedTime / 1000) % 60);

        //Padding for output, if necessary.
        hours = (Integer.parseInt(hours) < 10) ? "0" + hours : hours;
        minutes = (Integer.parseInt(minutes) < 10) ? "0" + minutes : minutes;
        seconds = (Integer.parseInt(seconds) < 10) ? "0" + seconds : seconds;

        embedBuilder.addField("Ping", event.getJDA().getGatewayPing() + "ms", true);
        embedBuilder.addField("Uptime", hours + ":" + minutes + ":" + seconds, true);
        embedBuilder.addField("Server Count", Integer.toString(event.getJDA().getGuilds().size()), true);
        embedBuilder.addField("Status", event.getJDA().getStatus().name(), true);
        embedBuilder.addField("Messages Processed", Long.toString(Main.messageCount), true);
        embedBuilder.addField("Commands Processed", Long.toString(Main.commandCount), true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
