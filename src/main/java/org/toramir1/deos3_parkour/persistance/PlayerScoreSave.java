package org.toramir1.deos3_parkour.persistance;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.toramir1.deos3_parkour.PlayerScore;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.toramir1.deos3_parkour.Deos3Parkour.SCORE_SERVICE;

public class PlayerScoreSave {
    private static final Gson GSON = new GsonBuilder().create();

    public static void savePlayerScores(MinecraftServer server) throws IOException {
        List<PlayerScore> scores = SCORE_SERVICE.getScores();

        String scoresAsJson = GSON.toJson(scores);

        Path worldRoot = server.getWorldPath(LevelResource.ROOT);
        Path modDir = worldRoot.resolve("parkourScores");
        Files.createDirectories(modDir);

        Path jsonPath = modDir.resolve("scores.json");
        try (FileWriter writer = new FileWriter(jsonPath.toFile())) {
            writer.write(scoresAsJson);
        }
    }

    public static List<PlayerScore> readPlayerScores(MinecraftServer server) {
        Path worldRoot = server.getWorldPath(LevelResource.ROOT);
        Path modDir = worldRoot.resolve("parkourScores");
        Path jsonPath = modDir.resolve("scores.json");

        if (!Files.exists(jsonPath)) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<PlayerScore>>() {
        }.getType();
        List<PlayerScore> scores = new ArrayList<>();
        try (FileReader reader = new FileReader(jsonPath.toFile())) {
            scores = GSON.fromJson(reader, listType);
        } catch (IOException e) {
            LogUtils.getLogger().error(e.getMessage());
        }
        return scores;
    }
}
