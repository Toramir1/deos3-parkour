package org.toramir1.deos3_parkour;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.toramir1.deos3_parkour.service.RunService;
import org.toramir1.deos3_parkour.service.ScoreService;
import org.toramir1.deos3_parkour.service.ScoreboardService;

import java.io.IOException;

import static org.toramir1.deos3_parkour.persistance.PlayerScoreSave.readPlayerScores;
import static org.toramir1.deos3_parkour.persistance.PlayerScoreSave.savePlayerScores;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Deos3Parkour.MODID)
public class Deos3Parkour {
    public static final String MODID = "deos3_parkour";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RunService RUN_SERVICE = new RunService();
    public static final ScoreService SCORE_SERVICE = new ScoreService();
    public static final ScoreboardService SCOREBOARD_SERVICE = new ScoreboardService();

    public Deos3Parkour(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");

        MinecraftServer server = event.getServer();
        SCORE_SERVICE.setScore(readPlayerScores(server));
        SCOREBOARD_SERVICE.initScoreboard(server);
    }

    @SubscribeEvent
    public void serverStopEvent(ServerStoppingEvent event) throws IOException {
        MinecraftServer server = event.getServer();
        savePlayerScores(server);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
