package com.akibot.commands.administration;

/*
	* AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
	*
	* Leave
	* Makes AkiBot leave a server.
	* Takes in format -ab leave
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;
import static com.akibot.commands.Category.MUSIC;

public class LeaveCommand extends BaseCommand {
	public LeaveCommand(){
		super(ADMIN, "`leave` - Makes AkiBot leave a server.", "`leave`: Makes AkiBot leave this server.", "Leave");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		GuildObject guild = Main.guildMap.get(event.getGuild().getId());
		Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

		//Ensures AkiBot is connected to voice before continuing
		if(!isMod(guild, getCategory(), event)){
			return;
		}

		switch(args.length){
			case 0:
				event.getChannel().sendMessage(":wave: See ya! Remember to join my support server if you would like to re-invite me: https://discord.gg/BHgyQQ6").complete();
				event.getGuild().leave().queue();
				return;
			default:
				event.getChannel().sendMessage("Invalid format! Type `-ab help leave` for more info.").queue();
		}
	}
}
