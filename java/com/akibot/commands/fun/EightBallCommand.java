package com.akibot.commands.fun;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static com.akibot.commands.Category.FUN;

public class EightBallCommand extends BaseCommand {
    public EightBallCommand() {
        super(FUN, "`8ball` - Responds with an 8-ball response.", "`8ball <message>`: " +
                "Responds with an 8-ball response to the message.", "8ball");
        fillResponses();
    }

    private ArrayList<String> responses = new ArrayList<>();

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));

        if (args.length == 0) {
            event.getChannel().sendMessage("I can't respond to nothing...").queue();
        } else {
            int num = new Random().nextInt(responses.size());
            event.getChannel().sendMessage(responses.get(num)).queue();
        }
    }

    private void fillResponses() {
        try {
            Scanner stream = new Scanner(new File("resources\\8Ball.txt")).useDelimiter("\\n");
            while (stream.hasNext()) {
                responses.add(stream.next().trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find 8ball.txt!");
            responses.add("Error loading 8ball.txt, can't respond :frowning:");
        }
    }
}
