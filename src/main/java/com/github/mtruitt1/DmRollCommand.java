package com.github.mtruitt1;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;

public class DmRollCommand {
    public static SlashCommandBuilder builder() {
        return SlashCommand.with("dmroll", "Hidden roll which can be revealed or partially revealed.",
                        Arrays.asList(
                                SlashCommandOption.create(SlashCommandOptionType.STRING, "dice", "All the dice to roll (Plus modifiers), separated by spaces", true)
                        ))
                .setDefaultPermission(false);
    }
}
