package cn.taskeren.minequery.command.client_pos;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.util.math.Vec3d;

public class ClientDefaultPosArgument implements ClientPosArgument {

	private final CoordinateArgument x;
	private final CoordinateArgument y;
	private final CoordinateArgument z;

	public ClientDefaultPosArgument(CoordinateArgument x, CoordinateArgument y, CoordinateArgument z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vec3d toAbsolutePos(FabricClientCommandSource source) {
		Vec3d vec3d = source.getPlayer().getPos();
		return new Vec3d(this.x.toAbsoluteCoordinate(vec3d.x), this.y.toAbsoluteCoordinate(vec3d.y), this.z.toAbsoluteCoordinate(vec3d.z));
	}
}
