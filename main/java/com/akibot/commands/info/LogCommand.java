package com.akibot.commands.info;

/*
 	* AkiBot v3.1.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
 	* 
 	* Log
 	* Only works when I call the command, and is not listed in commands or help outputs. PMs me the log (useful if I'm away from PC but want the log).
 	* Takes in format -ab log
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;

import static com.akibot.commands.Category.INFO;

public class LogCommand extends BaseCommand {
	public LogCommand(){
		super(INFO, "`log` - PMs log of activity since last startup.", "`log`: PMs log of activity since last startup.", "Log");
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
					event.getAuthor().openPrivateChannel().queue(m -> {
						try{
							m.sendFile(Main.log, null).queue();
						}catch(IOException e){
							e.printStackTrace();
						}
					});
				default:
					event.getChannel().sendMessage("Invalid format. Type `-ab log`.").queue();
			}
		}else{
		    event.getChannel().sendMessage("Sorry, this command is restricted to my developer only.").queue();
        }
	}
}
