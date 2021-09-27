package com.github.mtruitt1;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;
import java.util.List;

public class D20RollCommands {
    public static SlashCommandBuilder d20() {
        return SlashCommand.with("d20", "Rolls a d20 plus an optional modifier and comment.", d20RollOptions());
    }

    public static SlashCommandBuilder adv() {
        return SlashCommand.with("adv", "Rolls a d20 with ADVANTAGE plus an optional modifier and comment.", d20RollOptions());
    }

    public static SlashCommandBuilder dis() {
        return SlashCommand.with("dis", "Rolls a d20 with DISADVANTAGE plus an optional modifier and comment.", d20RollOptions());
    }

    public static List<SlashCommandOption> d20RollOptions() {
        return Arrays.asList(
                SlashCommandOption.create(SlashCommandOptionType.INTEGER, "modifier", "Your modifier for the roll. Put 0 if 0.", true),
                SlashCommandOption.create(SlashCommandOptionType.STRING, "comment", "Your comment for this roll, if any.", false)
        );
    }
}
