package com.akibot.commands.fun;

/*
 	* AkiBot v3.1.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
 	* 
 	* Roll
 	* Returns a random value (1-6 based on nextInt()), simulating a dice roll (up to 100 times per command) and displaying the total/average.
 	* Takes in format -ab roll numberHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;
import java.util.Random;

import static com.akibot.commands.Category.FUN;

public class RollCommand extends BaseCommand {
	public RollCommand(){
		super(FUN, "`roll` - Rolls a 6-sided dice.", "`roll numberHere`: Rolls a dice, and displays the total/average.\n\nIf no number is passed, only one roll will happen.", "Roll");
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
				event.getChannel().sendMessage("You rolled a " + (rng.nextInt(6)+1) + "!").queue();
				return;
			case 1:
				double roll, total = 0;
				//Verifies roll value is a number that is between 1-100 inclusive.
				try{
					roll = Integer.parseInt(args[0]);
				}catch(NumberFormatException e){
					event.getChannel().sendMessage("Invalid argument (rollNumber must be a number) - type `-ab help roll` for more info.").queue();
					return;
				}

				if(roll <= 0 || (roll > 100)){
					event.getChannel().sendMessage("Invalid argument (rollNumber must be 1-100) - type `-ab help roll` for more info.").queue();
					return;
				}

				//If safe, rolls and outputs total/average.
				for(int i = 0; i < roll; ++i){
					roll = rng.nextInt(6)+1;
					total += roll;
				}
				DecimalFormat format = new DecimalFormat("#.##");
				String average = format.format(total / roll);

				event.getChannel().sendMessage("You rolled " + (int)roll + " times. **Total:** " + (int)total + " **Average:** " + average).queue();
				return;
			default:
				event.getChannel().sendMessage("Invalid format! Type `-ab help roll` for more info.").queue();
		}
	}
}
