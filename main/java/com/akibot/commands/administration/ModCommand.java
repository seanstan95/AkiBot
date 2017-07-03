package com.akibot.commands.administration;

/*
    * AkiBot v3.1.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Mod
    * This is basically 3 commands in one - adding/removing mod status for a user, and listing all the mods for this server.
    * Takes in format -ab mod add modLevel @userMentionsHere, -ab mod remove @userMentionsHere, and -ab mod list
 */

import com.akibot.commands.BaseCommand;
import com.akibot.commands.ModLevel;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;
import static com.akibot.commands.ModLevel.FULL;
import static com.akibot.commands.ModLevel.MUSIC;

public class ModCommand extends BaseCommand {
    public ModCommand(){
        super(ADMIN, "`mod` - Manages AkiBot mods for this server.", "`mod add/remove modLevel @userMentionsHere`: Adds/removes the @mentioned users with the given modLevel. Valid mod levels are `music` and `full`\n\n" +
                "`music` mod level grants access to certain music commands but not administration commands.\n`full` mod level grants full access to all commands.\n\nTo change a pre-existing Mod's level, use the add command with the new modLevel." +
                "\n\n`mod list`: Lists all the AkiBot mods in this server, and their associated mod level.", "Mod");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        ModLevel modLevel;
        String output = "";

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                return;
            default:
                switch(args[0]){
                    case "add":
                        if(event.getMessage().getMentionedUsers().size() > 0){
                            for(User user : event.getMessage().getMentionedUsers()){
                                //Determine the modLevel this user will have
                                if(args[1].equalsIgnoreCase("music")){
                                    modLevel = MUSIC;
                                }else if(args[1].equalsIgnoreCase("full")){
                                    modLevel = FULL;
                                }else{
                                    event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                                    return;
                                }

                                //If the user is already added as a mod and the input modLevel is the same, skip the user. Otherwise, update that user's modLevel to the new one.
                                if(guild.getModList().containsKey(user.getId())){
                                    if(guild.getModList().get(user.getId()) != modLevel){
                                        guild.changeMod(user.getId(), modLevel, true, false);
                                        output = output.concat(", " + event.getGuild().getMember(user).getEffectiveName());
                                    }else {
                                        event.getChannel().sendMessage(event.getGuild().getMember(user).getEffectiveName() + " is already a mod with `" + modLevel.name() + "` mod level.").queue();
                                        return;
                                    }
                                }else{
                                    guild.changeMod(user.getId(), modLevel, false, false);
                                    output = output.concat(", " + event.getGuild().getMember(user).getEffectiveName());
                                }
                            }
                            event.getChannel().sendMessage("User(s) " + output.substring(2) + " added as mod(s).").queue();
                            Main.updateGuilds(false, null, null);
                        }else{
                            event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                        }
                        return;
                    case "remove":
                        if(event.getMessage().getMentionedUsers().size() > 0) {
                            for (User user : event.getMessage().getMentionedUsers()) {
                                //Confirms this user is in the mod list. If so, removes them.
                                if(guild.getModList().containsKey(user.getId())) {
                                    guild.changeMod(user.getId(), null, false, true);
                                    output = output.concat(", " + event.getGuild().getMember(user).getEffectiveName());
                                }else {
                                    event.getChannel().sendMessage(event.getGuild().getMember(user).getEffectiveName() + " is not a mod.").queue();
                                    return;
                                }
                            }
                            event.getChannel().sendMessage("User(s) " + output.substring(2) + " removed from mod list.").queue();
                            Main.updateGuilds(false, null, null);
                        }else{
                            event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                        }
                        return;
                    case "list":
                        for(String key : guild.getModList().keySet()){
                            output = output.concat(event.getGuild().getMemberById(key).getEffectiveName() + ": " + guild.getModList().get(key).name() + "\n");
                        }
                        event.getChannel().sendMessage("List of mods (and their mod level):\n" + output).queue();
                        return;
                    default:
                        event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                }
        }
    }
}
