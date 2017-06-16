package com.akibot.commands.info;

/*
    * AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * ServerInfo
    * Outputs relevant information about the server.
    * Takes in format -ab server
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

import static com.akibot.commands.Category.INFO;

public class ServerCommand extends BaseCommand {
    public ServerCommand(){
        super(INFO, "`server` - Outputs info about the current server.", "`server`: Outputs info about the current server (channel count, user count, etc.).", "Server");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        List<Role> roleList;
        String roles = "";

        switch(args.length){
            case 0:
                roleList = event.getGuild().getRoles();
                for(Role role : roleList){
                    //Skips the @everyone role in output
                    if(!role.getName().equalsIgnoreCase("@everyone")){
                        roles = roles.concat(", " + role.getName());
                    }
                }
                embedOutput(event, roles.substring(2));
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help server` for more info.").queue();
        }
    }

    private void embedOutput(MessageReceivedEvent event, String roles){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.addField("Server Name", event.getGuild().getName(), true);
        embedBuilder.addField("Creation Date", formatTime(event.getGuild().getCreationTime(), event), true);
        embedBuilder.addField("Server Owner", event.getGuild().getOwner().getEffectiveName(), true);
        embedBuilder.addField("User Count", Integer.toString(event.getGuild().getMembers().size()), true);
        embedBuilder.addField("Role Count", Integer.toString(event.getGuild().getRoles().size()), true);
        embedBuilder.addField("Text Channels", Integer.toString(event.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", Integer.toString(event.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Role List", roles, true);
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
