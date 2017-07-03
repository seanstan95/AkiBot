package com.akibot.commands.info;

/*
 	* AkiBot v3.1.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
 	* 
 	* Commands
 	* Returns a list of all bot commands for the given category (listed in the enum Category).
 	* Takes in format -ab commands admin/fun/info/music
 */

import com.akibot.commands.BaseCommand;
import com.akibot.commands.Category;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.akibot.commands.Category.INFO;

public class CommandsCommand extends BaseCommand {
	public CommandsCommand(){
		super(INFO, "`commands` - Returns list of commands.", "`commands admin/fun/info/music`: Displays a list of admin/fun/info/music commands.", "Commands");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		if(event.getGuild() != null){
			Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
		}else{
			Main.updateLog("PM", "PM", event.getAuthor().getName(), getName(), formatTime(null, event));
		}

	    boolean safe = false;
		String output = "";

		switch(args.length){
			case 1:
				//Determines if args[0] is a valid category
				for(Category category : Category.values()){
					if(category.name().equalsIgnoreCase(args[0])){
					    safe = true;
					    break;
                    }
				}

				if(safe){
					//SortedSet here ensures the keySet is sorted for alphabetic command list.
                    SortedSet<String> keys = new TreeSet<>(Main.commands.keySet());
                    for(String key : keys){
                        //args[0] is confirmed to be a category at this point, so it's safe to compare. If the command is log or shutdown, skip (irrelevant to all users except me).
                        if(Main.commands.get(key).getCategory().name().equalsIgnoreCase(args[0]) && (!key.equalsIgnoreCase("log") && !key.equalsIgnoreCase("shutdown"))){
                            output = output.concat(Main.commands.get(key).getInfo() + "\n");
                        }
                    }
                    embedOutput(output.trim(), event);
                }else{
					event.getChannel().sendMessage("Invalid category - type `-ab help commands` for more info.").queue();
				}
				return;
			default:
				event.getChannel().sendMessage("Invalid format! Type `-ab help commands` for more info.").queue();
		}
	}

	private void embedOutput(String output, MessageReceivedEvent event){
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setAuthor("AkiBot " + Main.version, null,null);
		embedBuilder.setColor(Color.decode("#9900CC"));
		embedBuilder.addField("Commands:\n", output.trim(), false);
		embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
		embedBuilder.setThumbnail(Main.THUMBNAIL);
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}
