package com.akibot.core.audio;

/*
    * AkiBot v3.1.4 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * TrackInfo
    * Used in setting user data for Audio Tracks. Contains username of requester and the channel associated to it.
 */

import net.dv8tion.jda.core.entities.MessageChannel;

public class TrackInfo {
    private MessageChannel channel;
    private String requester;

    public TrackInfo(String requester, MessageChannel channel){
        this.requester = requester;
        this.channel = channel;
    }

    public String getRequester(){
        return requester;
    }

    public MessageChannel getChannel(){
        return channel;
    }
}
