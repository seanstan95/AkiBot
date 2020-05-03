package com.akibot.core.audio;

import net.dv8tion.jda.api.entities.MessageChannel;

public class TrackInfo {
    private MessageChannel channel;
    private String requester;

    public TrackInfo(String requester, MessageChannel channel) {
        this.requester = requester;
        this.channel = channel;
    }

    public String getRequester() {
        return requester;
    }

    public MessageChannel getChannel() {
        return channel;
    }
}
