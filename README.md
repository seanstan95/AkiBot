# AkiBot - Multi-purpose Discord bot
## Purpose
AkiBot was created by me in Spring 2017 as a way to test my knowledge of using Java in a larger, more structured application 
than what I have made in classes before. I spent most of my free time from March-July of 2017 working on this bot on-and-off adding 
more features and commands as I went. I have placed a high priority on maintaining very clean and efficient code wherever possible, 
which always brought me back to try to improve the code even more over time.

**NOTE**: This bot is no longer functional due to Discord overhauling their bot system to use slash commands. This codebase has not been updated to use those commands or a more modern updated wrapper library. I may create a new version of AkiBot in the future to learn how to do that all, but this version will exist on GitHub for archiving sake.

## Commands
AkiBot's commands can be summarized into 4 categories: Administration, Fun, Information, and Music. All commands to the bot are prefaced 
by the phrase "-ab", or by saying @AkiBot in place of "-ab".

### Administration
**ban <@user>**: Bans the mentioned user from the server.

**create <text/voice/role> <name>**: Creates a new text/voice channel or role.
  
**delete <text/voice/role> <name>**: Deletes a text/voice channel or role.

**kick <@user>**: Kicks the mentioned user from the server.

**leave**: Makes AkiBot leave the server.

**mod <add/remove/list> <@user>**: Manages AkiBot mods for the server.

**mute <@user>**: Mutes a user in voice.

**nickname <newNickname> <@user>**: Nicknames a user.
  
**prune <days>**: Kicks inactive users from the server.
  
**unmute <@user>**: Unmutes a user in voice.

### Fun
**8ball <message>**: Responds with an 8-ball response.
  
**roll <number>**: Rolls a 6-sided dice.
  
**rps <rock/paper/scissors>**: Plays a round of Rock, Paper, Scissors with AkiBot.

### Info
**commands <admin/fun/info/music>**: Displays a list of commands per-category.

**help <commandName>**: Gives basic information about AkiBot, or displays help text for a command.
  
**server**: Displays information about the server.

**status**: Displays information about AkiBot.

**user <@user>**: Displays info about the mentioned user.

### Music
**pause**: Pauses the current song.

**play <link/searchTerms>**: Queues a song for playback, or unpauses a paused song.

**queue <reset>**: Displays up to 10 songs from the queue, or empties the queue.
  
**remove <songNumber>**: Removes a song from the queue.
  
**seek <mm:ss>**: Skips to the given time in the current song. 

**skip**: Skips the current song.

**song**: Displays information about the current song.

**stop**: Stops the current song.

**voice <join/leave>**: Controls AkiBot joining/leaving voice channels.

**volume <newVolume>**: Changes the current volume.
