package fr.Shiranuit.LogForJustice.Dimension;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

public class TeleporterDim implements ITeleporter {

	public final double posX, posY, posZ;
	public final int dim;
	
	public TeleporterDim(double x, double y, double z, int dim) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.dim = dim;
	}
	
	public TeleporterDim(Entity entity) {
		this(entity.posX, entity.posY, entity.posZ, entity.dimension);
	}
	
	public TeleporterDim(BlockPos pos, int dim) {
		this(pos.getX(), pos.getY(), pos.getZ(), dim);
	}
	
	public TeleporterDim(Vec3d pos, int dim) {
		this(pos.x, pos.y, pos.z, dim);
	}
	
	public TeleporterDim(TeleporterDim tpDim) {
		this(tpDim.posX, tpDim.posY, tpDim.posZ, tpDim.dim);
	}
	
	public TeleporterDim(Position pos) {
		this(pos.getVec(), pos.getDim());
	}
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		entity.motionX = entity.motionY = entity.motionZ = 0D;
		entity.fallDistance = 0F;
		if (entity instanceof EntityPlayerMP && ((EntityPlayerMP) entity).connection != null)
		{
			((EntityPlayerMP) entity).connection.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
		}
		else
		{
			entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
		}
	}
	
	@Nullable
	public Entity teleport(@Nullable Entity entity)
	{
		if (entity == null || entity.world.isRemote)
		{
			return entity;
		}

		if (dim != entity.dimension)
		{
			return entity.changeDimension(dim, this);
		}

		placeEntity(entity.world, entity, entity.rotationYaw);
		return entity;
}

}
