package com.github.mtruitt1;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandListener {
    public static void Add(DiscordApi api) {
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            System.out.println("Received command: " + slashCommandInteraction.getCommandName());
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("rollhelp")) {
                slashCommandInteraction.createImmediateResponder()
                        .setContent("```Syntax for DnDBot:\n\tA*_Bh_C_dX_+Y or A*_Bl_C_dX_+Y (Replace all underscores with spaces)" +
                                "\n\t\tA* = How many times to repeat everything which follows the *. Optional" +
                                "\n\t\tBh or Bl = Number of dice to keep for keep highest (h) B or keep lowest (l) B rolls. Optional" +
                                "\n\t\tC = The total number of dice for the roll. Optional" +
                                "\n\t\tdX = The dice size to roll. Not optional" +
                                "\n\t\t+Y = An additional modifier for the roll. Multiplied by A*. Optional```")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                return;
            }
            String dice = "";
            String modifier = "NONE";
            boolean is_dm = slashCommandInteraction.getCommandName().equalsIgnoreCase("dmroll");
            switch (slashCommandInteraction.getCommandName()) {
                case "roll":
                case "dmroll":
                    dice = slashCommandInteraction.getOptionStringValueByName("dice").orElse("");
                    break;
                case "d20":
                    dice = "1d20";
                    modifier = "";
                    break;
                case "adv":
                    dice = "1h2d20";
                    modifier = "";
                    break;
                case "dis":
                    dice = "1l2d20";
                    modifier = "";
                    break;
            }
            if (modifier.length() == 0) {
                modifier = slashCommandInteraction.getOptionIntValueByName("modifier").orElse(0).toString();
                if (!modifier.contains("-")) {
                    modifier = "+" + modifier;
                }
                dice += modifier;
            }
            String comment = "";
            if (!is_dm) {
                comment = slashCommandInteraction.getOptionStringValueByName("comment").orElse("");
            }
            RollResponse response = new RollResponse(slashCommandInteraction.getUser().getMentionTag(), dice, comment, slashCommandInteraction.getChannel().get());
            if (is_dm) {
                Main.last_dm_roll = response;
                slashCommandInteraction.createImmediateResponder()
                        .setContent(response.fullResponse)
                        .addComponents(
                                ActionRow.of(Button.danger("reroll", "Reroll"),
                                        Button.secondary("partial", "Partially reveal"),
                                        Button.success("full", "Fully reveal")))
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            } else {
                slashCommandInteraction.createImmediateResponder()
                        .setContent(response.fullResponse)
                        .respond();
            }
        });
    }
}
