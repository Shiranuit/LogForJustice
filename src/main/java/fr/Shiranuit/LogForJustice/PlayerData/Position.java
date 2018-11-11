package fr.Shiranuit.LogForJustice.PlayerData;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Position {
	private Vec3d vec;
	private int dimension;
	public Position(Vec3d vec, int dimension) {
		this.vec = vec;
		this.dimension = dimension;
	}
	
	public Position(BlockPos pos, int dimension) {
		this.vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
		this.dimension = dimension;
	}
	
	public Position(double x, double y, double z, int dimension) {
		this.vec = new Vec3d(x,y,z);
		this.dimension = dimension;
	}
	
	public Position(Entity entity) {
		this.vec = entity.getPositionVector();
		this.dimension = entity.dimension;
	}
	
	public Vec3d getVec() {
		return this.vec;
	}
	
	public int getDim() {
		return this.dimension;
	}
	
}
