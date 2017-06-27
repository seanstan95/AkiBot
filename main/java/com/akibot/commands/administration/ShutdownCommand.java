package com.akibot.commands.administration;

/*
 	* AkiBot v3.0.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
 	* 
 	* Shutdown
 	* Shuts down AkiBot.
 	* Takes in format -ab shutdown
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class ShutdownCommand extends BaseCommand {
	public ShutdownCommand(){
		super(ADMIN, "`shutdown` - Shuts down AkiBot.", "`shutdown`: Shuts down AkiBot.", "Shutdown");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		if(event.getGuild() != null){
			Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
		}else{
			Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
		}

		if(event.getAuthor().getId().equalsIgnoreCase("85603224790265856")){
			switch(args.length){
				case 0:
					//Confirms that the user is the bot owner (myself). If so, shuts down the JDA instance.
					event.getChannel().sendMessage(":wave: Shutting down.").complete();
					event.getJDA().shutdown();
					return;
				default:
					event.getChannel().sendMessage("Invalid format. Type `-ab shutdown`.").queue();
			}
		}else{
			event.getChannel().sendMessage("Sorry, this command is restricted to my developer only.").queue();
		}
	}
}
