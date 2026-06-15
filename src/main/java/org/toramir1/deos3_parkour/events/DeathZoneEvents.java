package org.toramir1.deos3_parkour.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static org.toramir1.deos3_parkour.Deos3Parkour.MODID;
import static org.toramir1.deos3_parkour.Deos3Parkour.RUN_SERVICE;

@EventBusSubscriber(modid = MODID)
public class DeathZoneEvents {

    @SubscribeEvent
    public static void tickEvent(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        BlockState blockState = player.getBlockStateOn();

        if (blockState.is(Blocks.BARRIER) || blockState.is(Blocks.MAGMA_BLOCK)) {
            RUN_SERVICE.teleportPlayerToCheckpoint(player);
        }
    }

    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) {
        ItemStack item = event.getItemStack();
        Player player = event.getEntity();

        if (item.is(Items.RED_DYE)) {
            RUN_SERVICE.teleportPlayerToCheckpoint(player);
        }
    }
}
