package com.akibot.commands.fun;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.util.Random;

import static com.akibot.commands.Category.FUN;

public class RollCommand extends BaseCommand {
	public RollCommand() {
		super(FUN, "`roll` - Rolls a 6-sided dice.", "`roll <number>`: Rolls a dice, " +
				"and displays the total/average. The number given must be in the range of 1-100." +
				"\n\nIf no number is given, only one roll will happen.", "Roll");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
				event.getAuthor().getName(), getName(), formatTime(null, event));
		Random rng = new Random();

		if (args.length == 0) {
			event.getChannel().sendMessage("You rolled a " + (rng.nextInt(6) + 1) + "!").queue();
		} else if (args.length > 1) {
			event.getChannel().sendMessage("Invalid format! Type `-ab help roll` for more info.").queue();
		} else {
			double roll, total = 0;

			//Verifies roll value as a number that is in 1-100 range.
			try {
				roll = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage("Invalid format! Type `-ab help roll` for more info.").queue();
				return;
			}

			if (roll <= 0 || (roll > 100)) {
				event.getChannel().sendMessage("Invalid format! Type `-ab help roll` for more info.").queue();
				return;
			}

			//If safe, rolls and outputs total/average.
			for (int i = 0; i < roll; ++i) {
				total += rng.nextInt(6)+1;
			}

			DecimalFormat format = new DecimalFormat("#.##");
			String average = format.format(total / roll);
			event.getChannel().sendMessage("You rolled " + (int) roll + " times. **Total:** " +
					(int) total + " **Average:** " + average).queue();
		}
	}
}
