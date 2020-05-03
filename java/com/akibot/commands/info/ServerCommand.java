package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

import static com.akibot.commands.Category.INFO;

public class ServerCommand extends BaseCommand {
    public ServerCommand() {
        super(INFO, "`server` - Displays info about the server.", "`server`: Displays info " +
                "about the server.", "Server");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));
        String roles = "";

        if (args.length == 0) {
            List<Role> roleList = event.getGuild().getRoles();
            for (Role role : roleList) {
                //Skips the @everyone role in output
                if (!role.getName().equalsIgnoreCase("@everyone")) {
                    roles = roles.concat(", " + role.getName());
                }
            }
            embedOutput(event, roles.substring(2));
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help server` for more info.").queue();
        }
    }

    private void embedOutput(GuildMessageReceivedEvent event, String roles) {
        EmbedBuilder embedBuilder = fillEmbed(new EmbedBuilder(), event);

        embedBuilder.addField("Server Name", event.getGuild().getName(), true);
        embedBuilder.addField("Creation Date", formatTime(event.getGuild().getTimeCreated(), event), true);
        embedBuilder.addField("Server Owner", event.getGuild().getOwner().getEffectiveName(), true);
        embedBuilder.addField("User Count", Integer.toString(event.getGuild().getMembers().size()), true);
        embedBuilder.addField("Role Count", Integer.toString(event.getGuild().getRoles().size()), true);
        embedBuilder.addField("Text Channels", Integer.toString(event.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", Integer.toString(event.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Role List", roles, true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
