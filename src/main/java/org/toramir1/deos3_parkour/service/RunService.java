package org.toramir1.deos3_parkour.service;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.toramir1.deos3_parkour.run.Run;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.toramir1.deos3_parkour.Deos3Parkour.SCOREBOARD_SERVICE;

public class RunService {
    private final Map<UUID, Run> currentRuns = new HashMap<>();

    public RunService() {
    }

    public void startRun(Player player) {
        if (!currentRuns.containsKey(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("notification.run.start"));
        }
        Run run = currentRuns.computeIfAbsent(player.getUUID(), k -> new Run(player));
        run.startRun();
    }

    public void endRun(Player player) {
        Run run = currentRuns.get(player.getUUID());
        if (run != null) {
            player.sendSystemMessage(Component.translatable("notification.run.end"));
            run.endRun();
            removeRun(player);
            SCOREBOARD_SERVICE.updateScoreboard();
        }
    }

    public void stopRun(Player player) {
        Run run = currentRuns.get(player.getUUID());
        if (run != null) {
            run.stopRun();
            removeRun(player);
        }
    }

    public void removeRun(Player player) {
        Run run = currentRuns.remove(player.getUUID());
        if (run != null) {
            run.endRun();
        }
    }

    public void addCheckpointToRun(Player player) {
        Run run = currentRuns.get(player.getUUID());
        if (run != null) {
            run.addCheckpoint(player.position());
        }
    }

    public void teleportPlayerToCheckpoint(Player player) {
        Run run = currentRuns.get(player.getUUID());
        if (run != null) {
            Vec3 checkPointPosition = run.getCheckpoint();
            if (checkPointPosition == null) return;
            player.teleportTo(checkPointPosition.x, checkPointPosition.y, checkPointPosition.z);
        }
    }
}
