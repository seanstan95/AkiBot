package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class ShutdownCommand extends BaseCommand {
	public ShutdownCommand() {
		super(ADMIN, "`shutdown` - Shuts down AkiBot.", "`shutdown`: Shuts down AkiBot.", "Shutdown");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		if (event.getAuthor().getId().equalsIgnoreCase("85603224790265856")) {
			if (args.length == 0) {
				event.getChannel().sendMessage(":wave: Shutting down.").complete();
				event.getJDA().shutdown();
			} else {
				event.getChannel().sendMessage("Invalid format. Type `-ab shutdown`.").queue();
			}
		}
	}
}
