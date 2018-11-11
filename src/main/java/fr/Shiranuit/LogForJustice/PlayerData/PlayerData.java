package fr.Shiranuit.LogForJustice.PlayerData;

import java.io.File;
import java.util.Date;

import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PlayerData {
	
	public EntityPlayer player;
	
	public Date lastSeen;
	public TempbanData tempban;
	public BlockPos lastPos;
	public BlockPos bedPos;
	
	private Configuration config;
	
	public PlayerData(EntityPlayer player) {
		this.player = player;
		this.config = new Configuration(new File(PlayerManager.modLogDir + File.separatorChar + player.getGameProfile().getId().toString()));
		this.load();
	}
	
	public PlayerData(String name) {
		this.config = new Configuration(new File(PlayerManager.modLogDir + File.separatorChar + name));
		this.load();
	}
	
	public PlayerData(Configuration config) {
		this.config = config;
		this.load();
	}
	
	public void load() {
		this.config.load();
		this.lastSeen = new Date(ObjectUtil.Long(this.config.get("infos", "seen", new Date().getTime()+"").getString()));
		
		int x = this.config.get("infos","x",0).getInt();
		int y = this.config.get("infos","y",0).getInt();
		int z = this.config.get("infos","z",0).getInt();
		this.lastPos = new BlockPos(x,y,z);
		
		 long bantime=ObjectUtil.Long(this.config.get("infos", "bantime", -1).getString());
		 if (bantime > -1) {
			 this.tempban = new TempbanData(new Date(bantime));
		 }
		 
		int bedx = this.config.get("infos","bedx",0).getInt();
		int bedy = this.config.get("infos","bedy",0).getInt();
		int bedz = this.config.get("infos","bedz",0).getInt();
		this.bedPos = new BlockPos(bedx, bedy, bedz);
		this.config.save();
	}
	
	public void save() {
		this.config.getCategory("infos").get("seen").set(this.lastSeen.getTime()+"");
		if (this.lastPos != null) {
			this.config.getCategory("infos").get("x").set(this.lastPos.getX());
			this.config.getCategory("infos").get("y").set(this.lastPos.getY());
			this.config.getCategory("infos").get("z").set(this.lastPos.getZ());
		}
		
		if (tempban != null) {
			this.config.getCategory("infos").get("bantime").set(this.tempban.time.getTime()+"");
		} else {
			this.config.getCategory("infos").get("bantime").set(-1);
		}
		
		if (this.bedPos != null) {
			this.config.getCategory("infos").get("bedx").set(this.bedPos.getX());
			this.config.getCategory("infos").get("bedy").set(this.bedPos.getY());
			this.config.getCategory("infos").get("bedz").set(this.bedPos.getZ());
		}
		this.config.save();
	}
	
	
	


}
