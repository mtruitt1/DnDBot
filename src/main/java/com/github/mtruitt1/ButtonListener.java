package com.github.mtruitt1;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.interaction.MessageComponentInteraction;

public class ButtonListener {
    public static void Add(DiscordApi api) {
        api.addMessageComponentCreateListener(event -> {
            MessageComponentInteraction messageComponentInteraction = event.getMessageComponentInteraction();
            String customId = messageComponentInteraction.getCustomId();
            switch (customId) {
                case "reroll":
                    Main.last_dm_roll.PerformRoll(messageComponentInteraction.getUser().getMentionTag());
                    messageComponentInteraction.createImmediateResponder()
                            .setContent(Main.last_dm_roll.fullResponse)
                            .addComponents(
                                    ActionRow.of(Button.danger("reroll", "Reroll"),
                                            Button.secondary("partial", "Partially reveal"),
                                            Button.success("full", "Fully reveal")))
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    break;
                case "partial":
                    messageComponentInteraction.createImmediateResponder()
                            .setContent("Partially revealing!")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    Main.last_dm_roll.channel.sendMessage(messageComponentInteraction.getUser().getMentionTag() + ": Roll total = **" + Main.last_dm_roll.rollTotal + "** (Details hidden)");
                    break;
                case "full":
                    messageComponentInteraction.createImmediateResponder()
                            .setContent("Fully revealing!")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    Main.last_dm_roll.channel.sendMessage(Main.last_dm_roll.fullResponse);
                    break;
            }
        });
    }
}
