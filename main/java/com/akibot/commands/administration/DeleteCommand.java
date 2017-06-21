package com.akibot.commands.administration;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Delete
    * This is basically 3 commands in one - deleting text/voice channels, and roles.
    * Takes in format -ab delete text/voice/role nameHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.akibot.commands.Category.ADMIN;

public class DeleteCommand extends BaseCommand {
    public DeleteCommand(){
        super(ADMIN, "`delete` - Deletes a text/voice channel, or role.", "`delete text/voice/role nameHere`: Deletes the given text/voice channel, or role." +
                          "\n\nThe delete request can not be called in the channel you wish to delete.\nIf there are multiple channels/roles with the same name, AkiBot will delete the first one by default.", "Delete");
    }

    public void action(String[] args, MessageReceivedEvent event){
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        String name = "";

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help delete` for more info.").queue();
                return;
            case 1:
                ArrayList<String> values = new ArrayList<>(Arrays.asList("text", "voice", "role"));
                if(values.contains(args[0])){
                    event.getChannel().sendMessage("No channel name specified - type `-ab help delete` for more info.").queue();
                }else{
                    event.getChannel().sendMessage("Invalid format! Type `-ab help delete` for more info.").queue();
                }
                return;
            default:
                //getting the channel/role name to delete
                for(int i = 1; i < args.length; ++i){
                    name = name.concat(args[i]);
                }

                switch(args[0]){
                    case "text":
                        if(event.getGuild().getTextChannelsByName(name, true).size() > 0 && !name.equalsIgnoreCase(event.getChannel().getName())){
                            event.getGuild().getTextChannelsByName(name, true).get(0).delete().queue();
                            event.getChannel().sendMessage("Text Channel \"" + name + "\" deleted.").queue();
                        }else{
                            event.getChannel().sendMessage("Invalid channel name - type `-ab help delete` for more info.").queue();
                        }
                        return;
                    case "voice":
                        if(event.getGuild().getVoiceChannelsByName(name, true).size() > 0){
                            event.getGuild().getVoiceChannelsByName(name, true).get(0).delete().queue();
                            event.getChannel().sendMessage("Voice Channel \"" + name + "\" deleted.").queue();
                        }else{
                            event.getChannel().sendMessage("Invalid channel name - type `-ab help delete` for more info.").queue();
                        }
                        return;
                    case "role":
                        if(event.getGuild().getRolesByName(name, true).size() > 0){
                            event.getGuild().getRolesByName(name, true).get(0).delete().queue();
                            event.getChannel().sendMessage("Role \"" + name + "\" deleted.").queue();
                        }else{
                            event.getChannel().sendMessage("Invalid role name - type `-ab help delete` for more info.").queue();
                        }
                        return;
                    default:
                        event.getChannel().sendMessage("Invalid format! Type `-ab help delete` for more info.").queue();
                }
        }
    }
}
