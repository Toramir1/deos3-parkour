package org.toramir1.deos3_parkour.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;

public class ParkourCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("parkour")
                    .requires(req -> req.hasPermission(4))
                        .then(Commands.literal("add")
                                .then(Commands.literal("start")
                                        .executes(context -> {
                                            createMarker(MarkerType.START, context.getSource());

                                            return 1;
                                        })
                                )
                                .then(Commands.literal("checkpoint")
                                        .executes(context -> {
                                            createMarker(MarkerType.CHECKPOINT, context.getSource());

                                            return 1;
                                        })
                                )
                                .then(Commands.literal("end")
                                        .executes(context -> {
                                            createMarker(MarkerType.END, context.getSource());

                                            return 1;
                                        })
                                )
                        )
        );
    }


    private static void createMarker(MarkerType type, CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        Marker startMarker = new Marker(EntityType.MARKER, level);
        startMarker.moveTo(BlockPos.containing(source.getPosition()).getBottomCenter());
        CompoundTag tag = new CompoundTag();
        tag.putString("parkourType", type.name());
        startMarker.getPersistentData().put("data", tag);
        level.addFreshEntity(startMarker);
    }
}
