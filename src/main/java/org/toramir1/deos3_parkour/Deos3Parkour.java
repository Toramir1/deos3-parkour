package org.toramir1.deos3_parkour;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerLifecycleEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.toramir1.deos3_parkour.commands.ParkourCommands;
import org.toramir1.deos3_parkour.commands.RunCommands;
import org.toramir1.deos3_parkour.service.BestTimeService;
import org.toramir1.deos3_parkour.service.RunService;
import org.toramir1.deos3_parkour.service.ScoreService;
import org.toramir1.deos3_parkour.service.ScoreboardService;

import java.io.IOException;

import static org.toramir1.deos3_parkour.persistance.PlayerScoreSave.readPlayerScores;
import static org.toramir1.deos3_parkour.persistance.PlayerScoreSave.savePlayerScores;

@Mod(Deos3Parkour.MODID)
public class Deos3Parkour {
    public static final String MODID = "deos3_parkour";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final RunService RUN_SERVICE = new RunService();
    public static final ScoreService SCORE_SERVICE = new ScoreService();
    public static final ScoreboardService SCOREBOARD_SERVICE = new ScoreboardService();
    public static final BestTimeService BEST_TIME_SERVICE = new BestTimeService();

    public Deos3Parkour(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
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
        RunCommands.register(event.getDispatcher());
        ParkourCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onLevelSave(LevelEvent.Save event) throws IOException {
        savePlayerScores(event.getLevel().getServer());
    }

     @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        BEST_TIME_SERVICE.startBestTimeDisplayScheduler(player);
     }
}
