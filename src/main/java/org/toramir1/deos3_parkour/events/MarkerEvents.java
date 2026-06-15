package org.toramir1.deos3_parkour.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.toramir1.deos3_parkour.commands.MarkerType;

import java.util.List;

import static org.toramir1.deos3_parkour.Deos3Parkour.MODID;
import static org.toramir1.deos3_parkour.Deos3Parkour.RUN_SERVICE;

@EventBusSubscriber(modid = MODID)
public class MarkerEvents {
	private static BlockPos lastPos = null;

	@SubscribeEvent
	public static void tickEvent(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();

		if (player.level().isClientSide()) return;

		BlockPos pos = player.blockPosition().below();
		AABB playerBoundingBox = player.getBoundingBox();
		if (!pos.equals(lastPos)) {
			lastPos = pos;
			List<Marker> markers = player.level().getEntitiesOfClass(Marker.class, playerBoundingBox.inflate(0.3));
			for (Marker marker : markers) {
				Vec3 markerPos = marker.position();
				AABB markerZone = new AABB(markerPos, markerPos).inflate(1.3);
				if (playerBoundingBox.intersects(markerZone) && player.blockPosition() != lastPos) {
					String parkourType = marker.getPersistentData().getCompound("data").getString("parkourType");
					if (parkourType.equals(MarkerType.START.name())) {
						RUN_SERVICE.startRun(player);
					} else if (parkourType.equals(MarkerType.CHECKPOINT.name())) {
						RUN_SERVICE.addCheckpointToRun(player);
					} else if (parkourType.equals(MarkerType.END.name())) {
						RUN_SERVICE.endRun(player);
					}
				}
			}
		}
	}
}
