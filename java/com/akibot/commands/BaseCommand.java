package com.akibot.commands;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class BaseCommand {
    private Category category;
    private String info, help, name;
    protected GuildObject guildObj;
    protected Guild guildControl;

    protected BaseCommand(Category category, String info, String help, String name) {
        this.category = category;
        this.info = info;
        this.help = help;
        this.name = name;
    }

    protected static String formatTime(OffsetDateTime offset, GuildMessageReceivedEvent event) {
        //Takes the time of the request and returns it in format M/dd/yyyy, h:mm:ss AM/PM UTC
        //Can take in an pre-determined OffsetDateTime (used in -ab user) or take the time from the event.
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a 'UTC'");

        return Objects.requireNonNullElseGet(offset, () -> event.getMessage().getTimeCreated()).format(format);
    }

    protected static boolean isMod(GuildObject guild, GuildMessageReceivedEvent event) {
        //Confirms the message author is entered in the list of mods
        if (guild.getModList().contains(event.getAuthor().getId())) {
            //If they are entered as a mod then they can do the command
            return true;
        } else {
            //If they aren't entered as a mod then they can't do the command
            event.getChannel().sendMessage("Sorry, you must be a mod to use this command.").queue();
            return false;
        }
    }

    protected static boolean isVoiceOk(Member bot, Member member, MessageChannel channel) {
        if (bot.getVoiceState().inVoiceChannel() && member.getVoiceState().inVoiceChannel()) {
            if (bot.getVoiceState().getChannel().getId().equals(member.getVoiceState().getChannel().getId())) {
                return true;
            } else {
                channel.sendMessage(":x: We must be in the same voice channel!").queue();
                return false;
            }
        } else {
            channel.sendMessage(":x: We must both be connected to a voice channel!").queue();
            return false;
        }
    }

    protected void setup(GuildMessageReceivedEvent event) {
        guildControl = event.getGuild();
        guildObj = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guildObj, event.getAuthor().getName(), getName(), formatTime(null, event));
    }

    public static EmbedBuilder fillEmbed(EmbedBuilder embed, GuildMessageReceivedEvent event) {
        embed.setAuthor("AkiBot " + Main.version, null, null);
        embed.setColor(Color.decode("#9900CC"));
        embed.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embed.setThumbnail(Main.THUMBNAIL);
        return embed;
    }

    public abstract void action(String[] args, GuildMessageReceivedEvent event);

    public Category getCategory() {
        return category;
    }

    public String getInfo() {
        return info;
    }

    public String getHelp() {
        return help;
    }

    public String getName() {
        return name;
    }
}
