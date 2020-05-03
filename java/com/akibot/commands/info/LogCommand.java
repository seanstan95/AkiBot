package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.INFO;

public class LogCommand extends BaseCommand {
	public LogCommand() {
		super(INFO, "`log` - DMs activity log.", "`log`: DMs activity log since last startup.", "Log");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		if (event.getAuthor().getId().equalsIgnoreCase("85603224790265856")) {
			if (args.length == 0) {
				event.getAuthor().openPrivateChannel().queue(m -> {
					try {
						m.sendFile(Main.log).queue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				event.getChannel().sendMessage("Invalid format. Type `-ab log`.").queue();
			}
		} else {
			event.getChannel().sendMessage("Sorry, this command is restricted to my developer only.").queue();
		}
	}
}
