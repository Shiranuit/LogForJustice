package fr.Shiranuit.LogForJustice.Area;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Selection {
	private BlockPos start = null;
	private BlockPos end = null;
	
	public Selection() {
		this.start = null;
		this.end = null;
	}
	
	public Selection(BlockPos start, BlockPos end) {
		this.start = start;
		this.end = end;
	}
	
	public void clear() {
		this.start = null;
		this.end = null;
	}
	
	public boolean isSet() {
		return start != null && end != null; 
	}
	
	public void setStart(BlockPos pos) {
		this.start = pos;
	}
	
	public void setEnd(BlockPos pos) {
		this.end = pos;
	}
	
	public BlockPos getStart() {
		return this.start;
	}
	
	public BlockPos getEnd() {
		return this.end;
	}
	
	public int totalBlocks() {
		if (isSet()) {
			int w = Math.abs(start.getX() - end.getX())+1;
			int h = Math.abs(start.getY() - end.getY())+1;
			int d = Math.abs(start.getZ() - end.getZ())+1;
			return w * h * d;
		}
		return 0;
	}
	
	public Vec3d getCenterPoint() {
		if (isSet()) {
			double x = (this.start.getX() + this.end.getX())/2D;
			double y = (this.start.getY() + this.end.getY())/2D;
			double z = (this.start.getZ() + this.end.getZ())/2D;
			return new Vec3d(x,y,z);
		}
		return null;
	}
	
	public Area toArea(int dimension) {
		if (isSet()) {
			return new Area(this, dimension, new HashMap<String, Flag>());
		}
		return null;
	}

}
