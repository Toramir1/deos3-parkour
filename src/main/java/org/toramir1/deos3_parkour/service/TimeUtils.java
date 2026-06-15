package org.toramir1.deos3_parkour.service;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.toramir1.deos3_parkour.PlayerScore;

public class TimeUtils {

    public static String getTimeWithPlayerNameAsText(PlayerScore score) {
        return getTextComponent(score.getPlayerName(), score.getScore()).getString();
    }

    private static String millisToMinutesAndSeconds(long millis) {
        return DurationFormatUtils.formatDuration(millis, "mm:ss:SSS");
    }

    private static Component getTextComponent(String playerName, long score) {
        MutableComponent namePart = Component.literal(playerName).withColor(14687012);
        MutableComponent separator = Component.literal(" - ").withColor(7829115);
        MutableComponent scorePart = getTimeAsText(score);

        return namePart.append(separator).append(scorePart);
    }

    public static MutableComponent getTimeAsText(long score) {
        return Component.literal(millisToMinutesAndSeconds(score)).withColor(16777215);
    }
}
