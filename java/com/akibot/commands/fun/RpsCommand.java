package com.akibot.commands.fun;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

import static com.akibot.commands.Category.FUN;

public class RpsCommand extends BaseCommand {
    public RpsCommand() {
        super(FUN, "`rps` - Plays a round of rock paper scissors.", "`rps <rock/paper/scissors>`: " +
                "Plays a round of rock paper scissors with AkiBot.", "Rps");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));

        if (args.length != 1) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help rps` for more info.").queue();
        } else {
            //Bot's choice
            int botChoice = new Random().nextInt(3);

            //User choice and determining outcome
            switch (args[0].toLowerCase()) {
                case "rock":
                    if (botChoice == 0) {
                        event.getChannel().sendMessage("I picked `rock`. Tie!").queue();
                    } else if (botChoice == 1) {
                        event.getChannel().sendMessage("I picked `paper`. You lose!").queue();
                    } else {
                        event.getChannel().sendMessage("I picked `scissors`. You win!").queue();
                    }
                    break;
                case "paper":
                    if (botChoice == 0) {
                        event.getChannel().sendMessage("I picked `rock`. You win!").queue();
                    } else if (botChoice == 1) {
                        event.getChannel().sendMessage("I picked `paper`. Tie!").queue();
                    } else {
                        event.getChannel().sendMessage("I picked `scissors`. You lose!").queue();
                    }
                case "scissors":
                    if (botChoice == 0) {
                        event.getChannel().sendMessage("I picked `rock`. You lose!").queue();
                    } else if (botChoice == 1) {
                        event.getChannel().sendMessage("I picked `paper`. You win!").queue();
                    } else {
                        event.getChannel().sendMessage("I picked `scissors`. Tie!").queue();
                    }
                default:
                    event.getChannel().sendMessage("Invalid format! Type `-ab help rps` for more info.").queue();
            }
        }
    }
}
