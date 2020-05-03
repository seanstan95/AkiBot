package com.akibot.core.bot;

import com.akibot.commands.BaseCommand;
import com.akibot.commands.administration.*;
import com.akibot.commands.fun.EightBallCommand;
import com.akibot.commands.fun.RollCommand;
import com.akibot.commands.fun.RpsCommand;
import com.akibot.commands.info.*;
import com.akibot.commands.music.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    //Bot Info Variables
    public static final String THUMBNAIL = "http://i.imgur.com/k3zVzOc.png", version = "v4.0.0";
    public static long commandCount = 0, messageCount = 0, startupTime;
    private static String token;

    //Main Variables (used in startup + updating files)
    public static AudioPlayerManager playerManager;
    private static File guildsFile;
    private static FileReader guildReader;
    private static PrintWriter guildWriter = null, logWriter = null;

    //Reference Variables (used in other classes)
    public static File log;
    public static HashMap<String, HashMap<String, BaseCommand>> categories = new HashMap<>();
    private static HashMap<String, BaseCommand> adminCommands = new HashMap<>();
    private static HashMap<String, BaseCommand> funCommands = new HashMap<>();
    private static HashMap<String, BaseCommand> infoCommands = new HashMap<>();
    private static HashMap<String, BaseCommand> musicCommands = new HashMap<>();
    public static HashMap<String, GuildObject> guildMap = new HashMap<>();

    public static void main(String[] args) {
        //Opens file input streams and startup api object.
        openFiles();

        try {
            JDA jda = JDABuilder.createDefault(token).addEventListeners(new CommandHandler())
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS).build();
            jda.setAutoReconnect(true);
            jda.getPresence().setActivity(Activity.of(ActivityType.DEFAULT, "AkiBot " + version + " | -ab help"));
            startupTime = System.currentTimeMillis() + 14400000;
        } catch (Exception e) {
            e.printStackTrace();
        }

        startup(startupTime);
    }

    private static void openFiles() {
        //Open files for reading input.
        try {
            guildsFile = new File("resources\\Guilds.txt");
            log = new File("Log.txt");
            logWriter = new PrintWriter(log);
            guildReader = new FileReader(guildsFile);
            token = new Scanner(new File("resources\\Token.txt")).useDelimiter("\\n").next().trim();
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file(s)!");
        }
    }

    private static void startup(long UtcTimeInMillis) {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        parseJSON();
        addCommands();

        //Logs the bot startup time.
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy, h:mm:ss a 'UTC'");
        Main.updateLog(null, "Username", "Command Name", sdf.format(UtcTimeInMillis));
    }

    private static void addCommands() {
        //Administration Commands
        adminCommands.put("ban", new BanCommand());
        adminCommands.put("create", new CreateCommand());
        adminCommands.put("delete", new DeleteCommand());
        adminCommands.put("kick", new KickCommand());
        adminCommands.put("leave", new LeaveCommand());
        adminCommands.put("mod", new ModCommand());
        adminCommands.put("mute", new MuteCommand());
        adminCommands.put("nickname", new NicknameCommand());
        adminCommands.put("prune", new PruneCommand());
        adminCommands.put("shutdown", new ShutdownCommand());
        adminCommands.put("unmute", new UnmuteCommand());

        //Fun Commands
        funCommands.put("8ball", new EightBallCommand());
        funCommands.put("roll", new RollCommand());
        funCommands.put("rps", new RpsCommand());

        //Info Commands
        infoCommands.put("commands", new CommandsCommand());
        infoCommands.put("help", new HelpCommand());
        infoCommands.put("log", new LogCommand());
        infoCommands.put("server", new ServerCommand());
        infoCommands.put("status", new StatusCommand());
        infoCommands.put("user", new UserCommand());

        //Music Commands
        musicCommands.put("pause", new PauseCommand());
        musicCommands.put("play", new PlayCommand());
        musicCommands.put("queue", new QueueCommand());
        musicCommands.put("remove", new RemoveCommand());
        musicCommands.put("seek", new SeekCommand());
        musicCommands.put("skip", new SkipCommand());
        musicCommands.put("song", new SongCommand());
        musicCommands.put("stop", new StopCommand());
        musicCommands.put("voice", new VoiceCommand());
        musicCommands.put("volume", new VolumeCommand());

        //Category Grouping
        categories.put("admin", adminCommands);
        categories.put("fun", funCommands);
        categories.put("info", infoCommands);
        categories.put("music", musicCommands);
    }

    private static void parseJSON() {
        //Parsing JSON from Guilds.JSON - first confirms Guilds.JSON has content
        //Then loops through each entry in the root array of guilds, putting the resulting GuildObject into guildMap
        if (guildsFile.length() == 0) {
            System.out.println("Empty JSON File!");
            return;
        }

        JSONArray fileArray = new JSONArray(new JSONTokener(guildReader));
        ArrayList<String> modList = new ArrayList<>();

        for (int i = 0; i < fileArray.length(); ++i) {
            //Get guild entry and save the id
            JSONObject guildEntry = (JSONObject) fileArray.get(i);
            JSONArray mods = (JSONArray) guildEntry.get("Mods");
            String guildId = (String) guildEntry.get("GuildId");
            String guildName = (String) guildEntry.get("GuildName");

            //Iterate through the list of mods
            for (Object mod : mods) {
                JSONObject modEntry = (JSONObject) mod;
                String modId = (String) modEntry.get("Id");
                modList.add(modId);
            }

            //Once all done iterating through list of mods, ready to put new guildObject into guildMap
            guildMap.put(guildId, new GuildObject(playerManager.createPlayer(), guildId, guildName, modList, (int) guildEntry.get("Volume")));
        }
    }

    public static void updateLog(GuildObject guildObj, String username, String command, String time) {
        if (guildObj == null) {
            logWriter.println("GuildName\tGuildId\tUsername\tCommand\tTime");
            logWriter.println("N/A\tN/A\tAkiBot\tStartup\t" + time);
        } else {
            logWriter.println(guildObj.getName() + "\t" + guildObj.getId() + "\t" + username + "\t" + command + "\t" + time);
        }
        logWriter.flush();
    }

    public static void updateGuilds(boolean leave, String name, String id) {
        //If the bot has left a server, update the list of GuildObjects before updating Guilds.txt
        if (leave) {
            System.out.printf("Leaving: %s (%s)", name, id);
            guildMap.remove(id);
        }

        try {
            guildWriter = new PrintWriter(guildsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Setting up structure - root array to which every GuildObject will be added to as a JSONObject
        JSONArray root = new JSONArray();

        for (String key : guildMap.keySet()) {
            root.put(guildMap.get(key).toJSON());
        }

        guildWriter.println(root.toString(2));
        guildWriter.flush();
    }
}