package com.akibot.commands;

/*
    * AkiBot v3.1.4 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * BaseCommand
    * Abstract class that is extened to all commands. Mainly used as a container for command information such as category, help, etc.
    * Secondary usage of this class is to provide useful static methods that all commands can access (formatting time, checking mod status, etc.).
 */

import com.akibot.core.bot.GuildObject;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.akibot.commands.Category.ADMIN;
import static com.akibot.commands.ModLevel.FULL;

public abstract class BaseCommand {
    private Category category;
    private String info, help, name;

    protected BaseCommand(Category category, String info, String help, String name){
        this.category = category;
        this.info = info;
        this.help = help;
        this.name = name;
    }

    protected static String formatTime(OffsetDateTime offset, MessageReceivedEvent event){
        //Takes the time of the request and returns it in format M/dd/yyyy, h:mm:ss AM/PM UTC
        //Can take in an pre-determined OffsetDateTime (used in -ab user) or take the time from the event.
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a 'UTC'");

        if(offset != null){
            return offset.format(format);
        }else{
            return event.getMessage().getCreationTime().format(format);
        }
    }

    protected static boolean isMod(GuildObject guild, Category category, MessageReceivedEvent event){
        //Confirms the message author is entered in the list of mods
        if(guild.getModList().containsKey(event.getAuthor().getId())){
            ModLevel modLevel = guild.getModList().get(event.getAuthor().getId());

            //All Moderation commands require FULL, so only need to check if modLevel is not FULL
            //If they are entered, then they can always do the MUSIC-level commands, so no need to check for that here
            if(category == ADMIN && modLevel != FULL){
                event.getChannel().sendMessage("Sorry, you must have `FULL` ModLevel to be able to use administration commands.").queue();
                return false;
            }
        }else{
            //If they aren't entered as a mod then they can't do the command
            event.getChannel().sendMessage("Sorry, you must be added as a mod for AkiBot to be able to use this command.").queue();
            return false;
        }

        //If here, then it's safe to allow command access
        return true;
    }

    protected static boolean isVoiceOk(GuildVoiceState botState, GuildVoiceState userState, MessageChannel channel){
        if(!botState.inVoiceChannel() || !userState.inVoiceChannel() || !botState.getChannel().getId().equalsIgnoreCase(userState.getChannel().getId())){
            channel.sendMessage(":x: We both must be in voice together - make sure we are connected to the same channel before calling a music command.").queue();
            return false;
        }
        return true;
    }

    public abstract void action(String[] args, MessageReceivedEvent event);

    public Category getCategory(){
        return category;
    }

    public String getInfo(){
        return info;
    }

    public String getHelp() {
        return help;
    }

    public String getName(){
        return name;
    }
}
