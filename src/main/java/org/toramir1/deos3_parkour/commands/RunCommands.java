package org.toramir1.deos3_parkour.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import static org.toramir1.deos3_parkour.Deos3Parkour.RUN_SERVICE;

public class RunCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("run")
                    .requires(req -> req.hasPermission(4))
                        .then(Commands.literal("start")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            RUN_SERVICE.startRun(player);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("end")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            RUN_SERVICE.endRun(player);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("stop")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            RUN_SERVICE.stopRun(player);
                                            return 1;
                                        })
                                )
                        )

                        .then(Commands.literal("checkpoint")
                                .then(Commands.literal("teleport")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    RUN_SERVICE.teleportPlayerToCheckpoint(player);
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}
