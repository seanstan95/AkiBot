package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;

import static com.akibot.commands.Category.INFO;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super(INFO, "`help` - Gives basic information about AkiBot.", "`help`: Displays a " +
                "basic description of AkiBot.\n`help <commandName>`: Displays information about " +
                "a specific command.", "Help");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));

        if (args.length == 0) {
            String output = "AkiBot is a multi-function bot, capable of server administration, a few games, " +
                    "general info (such as server or user details), and a fully-functioning music system for " +
                    "voice channels.\n\n`-ab` is the prefix for all commands (you can also @mention AkiBot). " +
                    "Type `-ab commands admin/fun/info/music` for a list of commands, by type.\nType " +
                    "`-ab help commandName` for command-specific help.";
            embedOutput(event, output, getName());
        } else if (args.length == 1) {
            boolean found = false;
            for (HashMap<String, BaseCommand> category : Main.categories.values()) {
                if (category.containsKey(args[0])) {
                    embedOutput(event, category.get(args[0]).getHelp(), category.get(args[0]).getName());
                    found = true;
                    break;
                }
            }
            if (!found) {
                event.getChannel().sendMessage("Command \"" + args[0] + "\" does not exist.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format. Type `-ab help help` for more info.").queue();
        }
    }

    private void embedOutput(GuildMessageReceivedEvent event, String output, String commandName) {
        EmbedBuilder embedBuilder = fillEmbed(new EmbedBuilder(), event);

        embedBuilder.addField("Command Info: \"" + commandName + "\"", output, true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
