package org.toramir1.deos3_parkour.service;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.toramir1.deos3_parkour.PlayerScore;

import java.util.List;

import static org.toramir1.deos3_parkour.Deos3Parkour.SCORE_SERVICE;

public class ScoreboardService {

    private static final String OBJECTIVE_NAME = "parkourScores";

    private Scoreboard scoreboard;

    public ScoreboardService() {
    }

    public void initScoreboard(MinecraftServer server) {
        scoreboard = server.getScoreboard();
        if (scoreboard.getObjective(OBJECTIVE_NAME) != null) return;
        scoreboard.addObjective(
                OBJECTIVE_NAME,
                ObjectiveCriteria.DUMMY,
                Component.literal("Leaderboard"),
                ObjectiveCriteria.RenderType.INTEGER,
                true,
                null
        );
        updateScoreboard();
    }

    public void updateScoreboard() {
        List<PlayerScore> scores = SCORE_SERVICE.getScores();
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);

        objective.setNumberFormat(BlankFormat.INSTANCE);

        for (PlayerScore playerScore : scores) {
            String playerName = playerScore.getPlayerName();
            long score = playerScore.getScore();
            ScoreAccess scoreAccess = scoreboard.getOrCreatePlayerScore(ScoreHolder.forNameOnly(playerName), objective);
            scoreAccess.display(Component.literal(playerName + "  " + millisToMinutesAndSeconds(score)));
        }
        scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, objective);
    }

    private String millisToMinutesAndSeconds(long millis) {
        return DurationFormatUtils.formatDuration(millis, "mm:ss:SSS");
    }
}
