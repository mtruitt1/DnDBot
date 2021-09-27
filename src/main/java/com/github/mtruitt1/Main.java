package com.github.mtruitt1;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
    public static RollResponse last_dm_roll;
    public static Random rand;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Insert your bot's token here
        String token = "ODQ3NTM2MjMwMTAzMjUzMDQy.YK_fnw.0N_peI0QLq9pxNEryFvbS2BuPCw";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println("DnDBot is now online.");
        api.updateActivity("Initializing...");
        rand = new Random();
        long[] active_servers = { 659137478808043522L, 868638575787397151L };
        SlashCommandBuilder dm_roll_builder = DmRollCommand.builder();
        SlashCommandBuilder[] public_commands = {
                RollCommand.builder(),
                D20RollCommands.d20(),
                D20RollCommands.adv(),
                D20RollCommands.dis(),
                SlashCommand.with("rollhelp", "Prints out roll syntax for reference.")
        };
        for (long active_server : active_servers) {
            Server server = api.getServerById(active_server).orElse(null);
            if (server == null) {
                continue;
            }
            List<SlashCommand> commands = api.getServerSlashCommands(server).join();
            for (SlashCommand command : commands) {
                System.out.println("Removing command " + command.getName() + " from " + server.getName());
                command.deleteForServer(server).get();
            }
            SlashCommand dm_roll = dm_roll_builder.createForServer(server).join();
            new SlashCommandPermissionsUpdater(server).setPermissions(Arrays.asList(SlashCommandPermissions.create(177971643761557504L, SlashCommandPermissionType.USER, true)))
                    .update(dm_roll.getId()).join();
            System.out.println("Added command " + dm_roll.getName() + " to " + server.getName());
            for (SlashCommandBuilder builder : public_commands) {
                SlashCommand command = builder.createForServer(server).join();
                System.out.println("Added command " + command.getName() + " to " + server.getName());
            }
        }
        SlashCommandListener.Add(api);
        ButtonListener.Add(api);
        System.out.println("DnDBot is done initialization.");
        api.updateActivity("D&D! Ready to roll!");
    }
}