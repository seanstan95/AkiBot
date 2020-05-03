package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class StopCommand extends BaseCommand {
    public StopCommand() {
        super(MUSIC, "`stop` - Stops the current song.", "`stop`: Stops song playback. Use `-ab play` " +
                "to resume audio playback.\nStopping and then resuming playback is effectively " +
                "the same as calling `-ab skip`.", "Stop");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            if (guildObj.getPlayer().getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song is playing!").queue();
            } else {
                guildObj.getPlayer().stopTrack();
                event.getChannel().sendMessage("Song stopped.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help stop` for more info.").queue();
        }
    }
}
