package org.toramir1.deos3_parkour.service;

import net.minecraft.world.entity.player.Player;
import org.toramir1.deos3_parkour.PlayerScore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.toramir1.deos3_parkour.Deos3Parkour.SCORE_SERVICE;

public class BestTimeService {
    private final Map<UUID, ScheduledFuture<?>> futures = new HashMap<>();

    public BestTimeService() {}

    public void startBestTimeDisplayScheduler(Player player) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        PlayerScore score = SCORE_SERVICE.getScoreOfPlayer(player);

        if (futures.containsKey(player.getUUID())) {
            return;
        }

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            player.displayClientMessage(TimeUtils.getTimeAsText(score.getScore()), true);
        }, 0, 1, TimeUnit.SECONDS);

        futures.put(player.getUUID(), future);
    }

    public void stopBestTimeSchedulerOfPlayer(Player player) {
        ScheduledFuture<?> future = futures.remove(player.getUUID());

        if (future != null) {
            future.cancel(true);
        }
    }
}
