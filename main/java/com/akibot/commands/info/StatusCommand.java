package com.akibot.commands.info;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Status
    * Outputs information about the bot's current status.
    * Takes in format -ab status
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

import static com.akibot.commands.Category.INFO;

public class StatusCommand extends BaseCommand {
    public StatusCommand(){
        super(INFO, "`status` - Outputs info about AkiBot's current status.", "`status`: Outputs info about AkiBot's current status.", "Status");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getGuild() != null){
            Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        }else{
            Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
        }

        switch(args.length){
            case 0:
                //Seems silly just immediately passing off to  embedOutput, but keeps the calculations out of the main action() method
                embedOutput(event);
                return;
            default:
                event.getChannel().sendMessage("Invalid format. Type `-ab botinfo`.").queue();
        }
    }

    private void embedOutput(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        long currentUtcTime = System.currentTimeMillis() + 14400000, elapsedTime = currentUtcTime - Main.startupTime;
        String hours = Long.toString(elapsedTime/3600000), minutes = Long.toString((elapsedTime/60000)%60), seconds = Long.toString((elapsedTime/1000)%60);

        //Padding for output, if necessary.
        if(Integer.parseInt(hours) < 10){
            hours = "0" + hours;
        }
        if(Integer.parseInt(minutes) < 10){
            minutes = "0" + minutes;
        }
        if(Integer.parseInt(seconds) < 10){
            seconds = "0" + seconds;
        }

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.addField("Ping", Long.toString(event.getJDA().getPing()) + "ms", true);
        embedBuilder.addField("Uptime", hours + ":" + minutes + ":" + seconds, true);
        embedBuilder.addField("Server Count", Integer.toString(event.getJDA().getGuilds().size()), true);
        embedBuilder.addField("Status", event.getJDA().getStatus().name(), true);
        embedBuilder.addField("Messages Processed", Long.toString(Main.messageCount), true);
        embedBuilder.addField("Commands Processed", Long.toString(Main.commandCount), true);
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
