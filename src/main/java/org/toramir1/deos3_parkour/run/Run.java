package org.toramir1.deos3_parkour.run;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.toramir1.deos3_parkour.PlayerScore;
import org.toramir1.deos3_parkour.service.TimeUtils;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.toramir1.deos3_parkour.Deos3Parkour.BEST_TIME_SERVICE;
import static org.toramir1.deos3_parkour.Deos3Parkour.SCORE_SERVICE;

public class Run {
    private LocalDateTime startTime;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Player player;
    private boolean isRunning = false;
    private Vec3 checkpoint;

    public Run(Player player) {
        this.player = player;
    }

    public void startRun() {
        if (isRunning) return;
        startTime = LocalDateTime.now();
        isRunning = true;
        BEST_TIME_SERVICE.stopBestTimeSchedulerOfPlayer(player);
        setupScheduler();
    }

    public void endRun() {
        saveScore();
        BEST_TIME_SERVICE.startBestTimeDisplayScheduler(player);
        stopRun();
    }

    public void stopRun() {
        isRunning = false;
        scheduler.shutdown();
    }

    public void addCheckpoint(Vec3 position) {
        checkpoint = position;
    }

    public Vec3 getCheckpoint() {
        return checkpoint;
    }

    private void setupScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            player.displayClientMessage(TimeUtils.getTimeAsText(duration.toMillis()), true);
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void saveScore() {
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        PlayerScore playerScore = new PlayerScore(player, duration.toMillis());
        SCORE_SERVICE.addOrUpdatePlayerScore(playerScore);
    }
}
