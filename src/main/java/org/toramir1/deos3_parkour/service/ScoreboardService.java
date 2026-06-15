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
import org.toramir1.deos3_parkour.PlayerScore;

import java.util.Comparator;
import java.util.List;

import static org.toramir1.deos3_parkour.Deos3Parkour.SCORE_SERVICE;
import static org.toramir1.deos3_parkour.service.TimeUtils.getTimeWithPlayerNameAsText;

public class ScoreboardService {

    private static final String OBJECTIVE_NAME = "parkourScores";

    private Scoreboard scoreboard;

    public ScoreboardService() {}

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
        List<PlayerScore> sortedScores = scores.stream().sorted(Comparator.comparingLong(PlayerScore::getScore).reversed()).limit(10).toList();
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);

        objective.setNumberFormat(BlankFormat.INSTANCE);

        for (PlayerScore playerScore : sortedScores) {
            String playerName = playerScore.getPlayerName();
            ScoreAccess scoreAccess = scoreboard.getOrCreatePlayerScore(ScoreHolder.forNameOnly(playerName), objective);
            scoreAccess.set(sortedScores.size() - sortedScores.indexOf(playerScore));
            scoreAccess.display(Component.literal(TimeUtils.getTimeWithPlayerNameAsText(playerScore)));
        }
        scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, objective);
    }
}
