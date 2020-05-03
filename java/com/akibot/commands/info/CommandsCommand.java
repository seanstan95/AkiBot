package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.commands.Category;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.akibot.commands.Category.INFO;

public class CommandsCommand extends BaseCommand {
	public CommandsCommand() {
		super(INFO, "`commands` - Displays list of commands.", "`commands <admin/fun/info/music>`: " +
				"Displays a list of admin/fun/info/music commands, and a brief description of each.", "Commands");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
				event.getAuthor().getName(), getName(), formatTime(null, event));
		String output = "";
		boolean found = false;

		if (args.length == 1) {
			//Determines if args[0] is a valid category
			for (Category category : Category.values()) {
				if (category.name().equalsIgnoreCase(args[0])) {
					found = true;
					break;
				}
			}

			if (!found) {
				event.getChannel().sendMessage("Invalid category - type `-ab help commands` for more info.").queue();
				return;
			}

			SortedSet<BaseCommand> commands = new TreeSet<>(Comparator.comparing(BaseCommand::getName));
			commands.addAll(Main.categories.get(args[0]).values());
			for (BaseCommand command : commands) {
				if (!command.getName().equalsIgnoreCase("log")
						&& !command.getName().equalsIgnoreCase("shutdown")) {
					output = output.concat(command.getInfo() + "\n");
				}
			}
			embedOutput(output.trim(), event);
		} else {
			event.getChannel().sendMessage("Invalid format! Type `-ab help commands` for more info.").queue();
		}
	}

	private void embedOutput(String output, GuildMessageReceivedEvent event) {
		EmbedBuilder embedBuilder = fillEmbed(new EmbedBuilder(), event);
		embedBuilder.addField("Commands:\n", output.trim(), false);
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}
