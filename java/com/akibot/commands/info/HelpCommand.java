package com.akibot.commands.info;

/*
 * AkiBot v3.1.5 by PhoenixAki: music + moderation bot for usage in Discord servers.
 *
 * Help
 * Central command for handling help values for commands. Passing no command name will return a generalized introduction to AkiBot and its functions.
 * Takes in format -ab help <commandName>
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;

import static com.akibot.commands.Category.INFO;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super(INFO, "`help` - Gives basic information about the bot.", "What do you think `Help` does? :thinking:", "Help");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getGuild() != null) {
            Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        } else {
            Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
        }

        String output;

        switch (args.length) {
            case 0:
                output = "AkiBot is a multi-function bot, capable of server administration, a few games, general info (such as server or user details), and a fully-functioning music system for voice channels.\n\n" +
                        "AkiBot is also a music bot, capable of playing music in voice channels with a fully functioning request queue system.\n\n`-ab` is the prefix for all commands (you can also @mention the bot)." +
                        "Type `-ab commands admin/fun/info/music` for a list of commands, by type.\nType `-ab help commandName` for command-specific help.\n\nYou can join AkiBot's support server [here](discord.gg/BHgyQQ6).";
                embedOutput(event, output, getName());
                return;
            case 1:
                //If there is a key (command name) that matches the input, output the help text for that command.
                if (Main.commands.containsKey(args[0])) {
                    embedOutput(event, Main.commands.get(args[0]).getHelp(), Main.commands.get(args[0]).getName());
                    return;
                }
            default:
                event.getChannel().sendMessage("Invalid format. Type `-ab help` or `-ab help command_name`.").queue();
        }
    }

    private void embedOutput(MessageReceivedEvent event, String output, String commandName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.addField("Command Info: \"" + commandName + "\"", output, true);
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
