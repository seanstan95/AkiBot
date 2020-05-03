package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import static com.akibot.commands.Category.MUSIC;

public class VoiceCommand extends BaseCommand {
    public VoiceCommand() {
        super(MUSIC, "`voice` - Controls AkiBot joining/leaving voice.", "`voice <join/leave>`: Summons " +
                "or dismisses AkiBot from voice channels. If a song is currently playing when AkiBot leaves, the " +
                "song is paused (not stopped entirely).", "Voice");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));

        if (args.length != 1) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help voice` for more info.").queue();
        } else {
            if (args[0].equalsIgnoreCase("join")) {
                if (!event.getMember().getVoiceState().inVoiceChannel() || event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
                    event.getChannel().sendMessage(":x: Either you're not in voice or I'm already in voice.").queue();
                } else {
                    AudioManager voice = event.getGuild().getAudioManager();
                    voice.openAudioConnection(event.getMember().getVoiceState().getChannel());
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
                    guildObj.getPlayer().setPaused(true);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            } else {
                event.getChannel().sendMessage("Invalid format! Type `-ab help voice` for more info.").queue();
            }
        }
    }
}
