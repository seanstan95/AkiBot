package com.akibot.commands.fun;

/*
    * AkiBot v3.1.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Rock
    * Plays a round of rock paper scissors with AkiBot.
    * Takes in format -ab rps rock/paper/scissors
*/

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

import static com.akibot.commands.Category.FUN;

public class RpsCommand extends BaseCommand {
    public RpsCommand(){
        super(FUN, "`rps` - Plays a round of rock paper scissors.", "`rps rock/paper/scissors`: Plays a round of rock paper scissors with me.", "Rps");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getGuild() != null){
            Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        }else{
            Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
        }

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help rps` for more info.").queue();
                return;
            case 1:
                //Determining bot's choice
                Random rng = new Random();
                int botRoll = rng.nextInt(3);

                //Processing user roll and determining winner (or tie)
                switch(args[0]){
                    case "rock":
                        if(botRoll == 0){
                            event.getChannel().sendMessage("I picked `rock`. Tie!").queue();
                        }else if(botRoll == 1){
                            event.getChannel().sendMessage("I picked `paper`. You lose!").queue();
                        }else{
                            event.getChannel().sendMessage("I picked `scissors`. You win!").queue();
                        }
                        break;
                    case "paper":
                        if(botRoll == 0){
                            event.getChannel().sendMessage("I picked `rock`. You win!").queue();
                        }else if(botRoll == 1){
                            event.getChannel().sendMessage("I picked `paper`. Tie!").queue();
                        }else{
                            event.getChannel().sendMessage("I picked `scissors`. You lose!").queue();
                        }
                    case "scissors":
                        if(botRoll == 0){
                            event.getChannel().sendMessage("I picked `rock`. You lose!").queue();
                        }else if(botRoll == 1){
                            event.getChannel().sendMessage("I picked `paper`. You win!").queue();
                        }else{
                            event.getChannel().sendMessage("I picked `scissors`. Tie!").queue();
                        }
                    default:
                        event.getChannel().sendMessage("Invalid format! Type `-ab help rps` for more info.").queue();
                }
        }
    }
}
