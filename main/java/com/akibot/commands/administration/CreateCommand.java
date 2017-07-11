package com.akibot.commands.administration;

/*
    * AkiBot v3.1.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Create
    * This is basically 3 commands in one - creating text channels, voice channels, and roles.
    * Takes in format -ab create text nameHere, -ab create voice nameHere, and -ab create role nameHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.akibot.commands.Category.ADMIN;

public class CreateCommand extends BaseCommand {
    public CreateCommand(){
        super(ADMIN, "`create` - Creates a new Text/Voice channel or role.", "`create text/voice/role nameHere`: Creates a new text/voice channel, or role, with the given name." +
                               "\n\n**Note**: Names must be alphanumeric (A-Z, 0-9) and - or _ only." + "\nYou can create multiple channels/roles per command by separating them with | (if separated with spaces, AkiBot will assume they are separate channels/roles)." , "Create");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildController controller = event.getGuild().getController();
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_|]+$");
        String name = "", output = "";

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0:
                event.getTextChannel().sendMessage("Invalid format! Type `-ab help create` for more info.").queue();
                return;
            case 1:
                ArrayList<String> values = new ArrayList<>(Arrays.asList("text", "voice", "role"));
                if(values.contains(args[0])){
                    event.getChannel().sendMessage("No channel name specified - type `-ab help create` for more info.").queue();
                }else{
                    event.getChannel().sendMessage("Invalid format! Type `-ab help create` for more info.").queue();
                }
                return;
            default:
                for(int i = 1; i < args.length; ++i){
                    name = (args[i].contains("|")) ? name.concat(args[i]) : name.concat(args[i]) + "|";
                }
                String[] names = name.split("\\|");

                if(NAME_PATTERN.matcher(name).find()) {
                    switch (args[0]) {
                        case "text":
                            for (String channelName : names) {
                                controller.createTextChannel(channelName).queue();
                                output = output.concat(", " + channelName);
                            }
                            event.getChannel().sendMessage("Text Channel(s) " + output.substring(2) + " created.").queue();
                            return;
                        case "voice":
                            for (String channelName : names) {
                                controller.createVoiceChannel(channelName).queue();
                                output = output.concat(", " + channelName);
                            }
                            event.getChannel().sendMessage("Voice Channel(s) " + output.substring(2) + " created.").queue();
                            return;
                        case "role":
                            for (String roleName : names) {
                                controller.createRole().setName(roleName).queue();
                                output = output.concat(", " + roleName);
                            }
                            event.getChannel().sendMessage("Role(s) " + output.substring(2) + " created.").queue();
                            return;
                        default:
                            event.getChannel().sendMessage("Invalid format! Type `-ab help create` for more info.").queue();
                    }
                }
        }
    }
}
