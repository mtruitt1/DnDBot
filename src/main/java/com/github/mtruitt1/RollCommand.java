package com.github.mtruitt1;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;

public class RollCommand {
    public static SlashCommandBuilder builder() {
        return SlashCommand.with("roll", "Rolls the specified set of dice, plus modifiers.",
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "dice", "All the dice to roll (Plus modifiers), separated by spaces", true),
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "comment", "Comment to attach to the roll", false)
                ));
    }
}
