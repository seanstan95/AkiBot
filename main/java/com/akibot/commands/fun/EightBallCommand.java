package com.akibot.commands.fun;

/*
 	* AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
 	* 
 	* 8ball
 	* Responds with an 8-ball response, taken at random from the list of responses read in on startup from 8ball.txt
 	* Takes in format -ab 8ball messageHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

import static com.akibot.commands.Category.FUN;

public class EightBallCommand extends BaseCommand {
    public EightBallCommand(){
        super(FUN, "`8ball` - Responds with an 8-ball response.", "`8ball messageHere`: Responds with an 8-ball response to the message.\n\nIf no message is passed, AkiBot will respond telling you to ask something and try again.", "8Ball");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getGuild() != null){
            Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        }else{
            Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
        }

        Random rng = new Random();

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("I can't respond to nothing - ask me something next time!").queue();
                return;
            default:
                event.getChannel().sendMessage(Main.eightBallResponses.get(rng.nextInt(Main.eightBallResponses.size()))).queue();
        }
    }
}
