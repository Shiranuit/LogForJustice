package fr.Shiranuit.LogForJustice.PlayerData;

import java.lang.reflect.Method;

import akka.japi.Function;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public class NBTPlayerData {
	
	public boolean mute = false;
	
	private EntityPlayer player;
	
	public NBTPlayerData(EntityPlayer player) {
		this.player = player;
		load();
	}
	
	public EntityPlayer getPlayer() {
		return this.player;
	}
	
	public void load() {
		if (Util.isConnected(this.player.getName())) {
			NBTTagCompound compound = this.player.getEntityData();
			NBTTagCompound lfj = new NBTTagCompound();
			if (compound.hasKey("LFJ", NBT.TAG_COMPOUND)) {
				lfj = compound.getCompoundTag("LFJ");
			}
			
			//
			this.mute = lfj.hasKey("mute") ? lfj.getBoolean("mute") : false;
		}
	}
	
	public void save() {
		if (Util.isConnected(this.player.getName())) {
			NBTTagCompound compound = this.player.getEntityData();
			NBTTagCompound lfj = new NBTTagCompound();
			if (compound.hasKey("LFJ", NBT.TAG_COMPOUND)) {
				lfj = compound.getCompoundTag("LFJ");
			}
			
			//
			lfj.setBoolean("mute", this.mute);
			
			//			
			this.player.getEntityData().setTag("LFJ", lfj);
		}
		
	}

}
