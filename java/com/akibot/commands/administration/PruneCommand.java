package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class PruneCommand extends BaseCommand {
    public PruneCommand() {
        super(ADMIN, "`prune` - Kicks inactive users from the server.", "`prune <days>`: Kicks " +
                "users in the server who have been offline for at least the given number of days." +
                "Provided day must be between 1-30.", "Prune");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 1) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help prune` for more info.").queue();
        } else {
            try {
                int days = Integer.parseInt(args[0]);
                if (days < 1 || days > 30) {
                    event.getChannel().sendMessage("Invalid format! Type `-ab help prune` for more info.").queue();
                    return;
                }
                guildControl.prune(days).queue(i -> event.getChannel().sendMessage("Number of users " +
                        "kicked: " + i).queue());
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help prune` for more info.").queue();
            }
        }
    }
}
