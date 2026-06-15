package org.toramir1.deos3_parkour.service;

import net.minecraft.world.entity.player.Player;
import org.toramir1.deos3_parkour.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.toramir1.deos3_parkour.Deos3Parkour.BEST_TIME_SERVICE;

public class ScoreService {
    private List<PlayerScore> scores = new ArrayList<>();

    public ScoreService() {
    }

    public void addOrUpdatePlayerScore(PlayerScore playerScore) {
        Optional<PlayerScore> oldScoreOptional = scores.stream().filter(score -> score.getId().equals(playerScore.getId())).findFirst();
        if (oldScoreOptional.isPresent()) {
            PlayerScore oldScore = oldScoreOptional.get();
            if (newScoreBetterThanOld(oldScore, playerScore)) {
                scores.set(scores.indexOf(oldScore), playerScore);
            }
        } else {
            scores.add(playerScore);
        }
    }

    public void setScore(List<PlayerScore> scores) {
        this.scores = scores;
    }

    public List<PlayerScore> getScores() {
        return scores;
    }

    public PlayerScore getScoreOfPlayer(Player player) {
        return scores.stream().filter(s -> Objects.equals(s.getPlayerName(), player.getName().getString())).findFirst().orElseThrow();
    }

    private boolean newScoreBetterThanOld(PlayerScore oldScore, PlayerScore newScore) {
        return oldScore.getScore() < newScore.getScore();
    }
}
