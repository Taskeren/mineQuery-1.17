package cn.taskeren.minequery.command.client_pos;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface ClientPosArgument {

	Vec3d toAbsolutePos(FabricClientCommandSource source);

	default BlockPos toAbsoluteBlockPos(FabricClientCommandSource source) {
		return new BlockPos(this.toAbsolutePos(source));
	}

}
