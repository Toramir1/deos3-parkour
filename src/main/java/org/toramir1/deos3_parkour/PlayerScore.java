package org.toramir1.deos3_parkour;

import net.minecraft.world.entity.player.Player;

public class PlayerScore {
    private String id;
    private String playerName;
    private long score;

    public PlayerScore(Player player, long score) {
        id = player.getStringUUID();
        playerName = player.getGameProfile().getName();
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getScore() {
        return score;
    }
}
