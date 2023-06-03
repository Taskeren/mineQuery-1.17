package cn.taskeren.minequery.command.client_pos;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public interface ClientPosArgument {

	Vec3d toAbsolutePos(FabricClientCommandSource source);

	default BlockPos toAbsoluteBlockPos(FabricClientCommandSource source) {
		var p = this.toAbsolutePos(source);
		return new BlockPos(new Vec3i((int) p.x, (int) p.y, (int) p.z));
	}

}
