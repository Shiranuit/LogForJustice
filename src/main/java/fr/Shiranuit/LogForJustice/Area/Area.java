package fr.Shiranuit.LogForJustice.Area;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Area {
	public int x;
	public int y;
	public int z;
	public int dx;
	public int dy;
	public int dz;
	public int dimensionID; 
	public HashMap<String, Flag> flags;
	public Area(int x, int y, int z, int dx, int dy, int dz, int dimensionID, HashMap<String, Flag> flags) {
		this.x = Math.min(x,dx);
		this.y = Math.min(y,dy);
		this.z = Math.min(z,dz);
		
		this.dx = Math.max(x,dx);
		this.dy = Math.max(y,dy);
		this.dz = Math.max(z,dz);
		
		this.dimensionID = dimensionID;
		this.flags = flags;
	}
	
	public Area(Selection selection, int dimensionID, HashMap<String, Flag> flags) {
		this.x = Math.min(selection.getStart().getX(),selection.getEnd().getX());
		this.y = Math.min(selection.getStart().getY(),selection.getEnd().getY());
		this.z = Math.min(selection.getStart().getZ(),selection.getEnd().getZ());
		
		this.dx = Math.max(selection.getStart().getX(),selection.getEnd().getX());
		this.dy = Math.max(selection.getStart().getY(),selection.getEnd().getY());
		this.dz = Math.max(selection.getStart().getZ(),selection.getEnd().getZ());
		
		this.dimensionID = dimensionID;
		this.flags = flags;
	}

	public boolean isIn(Entity ent) {
		if (ent.dimension == this.dimensionID && ent.posX >= this.x && ent.posX <= this.dx+1 && ent.posY >= this.y && ent.posY <= this.dy && ent.posZ >= this.z && ent.posZ <= this.dz+1) {
			return true;
		}
		return false;
	}
	
	public boolean isIn(World w, BlockPos pos) {
		if (w.provider.getDimension() == this.dimensionID && pos.getX() >= this.x && pos.getX() <= this.dx && pos.getY() >= this.y && pos.getY() <= this.dy && pos.getZ() >= this.z && pos.getZ() <= this.dz) {
			return true;
		}
		return false;
	}
	
	public boolean isIn(World w, Vec3d pos) {
		if (w.provider.getDimension() == this.dimensionID && pos.x >= this.x && pos.x <= this.dx+1 && pos.y >= this.y && pos.y <= this.dy && pos.z >= this.z && pos.z <= this.dz+1) {
			return true;
		}
		return false;
	}
}
