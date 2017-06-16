package com.akibot.commands.music;

/*
    * AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Voice
    * Handles AkiBot joining or leaving voice channels. Created to combine join/leave to free up LeaveCommand for leaving guilds instead.
    * Takes in format -ab voice join and -ab voice leave
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import static com.akibot.commands.Category.MUSIC;

public class VoiceCommand extends BaseCommand {
    public VoiceCommand(){
        super(MUSIC, "`voice` - Controls AkiBot joining/leaving voice.", "`voice join`: Summons AkiBot to a voice channel.\n`voice leave`: Makes AkiBot leave its voice channel.", "Voice");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help voice` for more info.").queue();
                return;
            case 1:
                if(args[0].equalsIgnoreCase("join")){
                    //Ensures AkiBot is connected to voice before continuing
                    if(!event.getMember().getVoiceState().inVoiceChannel() || event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()){
                        event.getChannel().sendMessage(":x: Either you're not in voice or I'm already in voice.").queue();
                    }else{
                        AudioManager voice = event.getGuild().getAudioManager();
                        voice.openAudioConnection(event.getMember().getVoiceState().getChannel());
                        event.getChannel().sendMessage(":heavy_check_mark: Joining voice channel \"" + event.getMember().getVoiceState().getChannel().getName() + "\".").queue();
                    }
                }else if(args[0].equalsIgnoreCase("leave")){
                    if(isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
                        event.getGuild().getAudioManager().closeAudioConnection();
                        event.getChannel().sendMessage(":wave: Left voice channel \"" + event.getGuild().getSelfMember().getVoiceState().getChannel().getName() + "\".").queue();
                    }
                }
        }
    }
}
