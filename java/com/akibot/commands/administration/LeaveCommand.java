package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class LeaveCommand extends BaseCommand {
	public LeaveCommand() {
		super(ADMIN, "`leave` - Makes AkiBot leave a server.", "`leave`: Makes AkiBot " +
				"leave this server.", "Leave");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		setup(event);

		//Ensures the user is of proper mod level to perform this command
		if (!isMod(guildObj, event)) {
			return;
		}

		if (args.length == 0) {
			event.getChannel().sendMessage(":wave: See ya!").complete();
			event.getGuild().leave().queue();
		} else {
			event.getChannel().sendMessage("Invalid format! Type `-ab help leave` for more info.").queue();
		}
	}
}
