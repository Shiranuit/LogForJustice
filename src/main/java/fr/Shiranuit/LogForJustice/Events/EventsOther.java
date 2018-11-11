package fr.Shiranuit.LogForJustice.Events;

import java.util.Date;
import java.util.HashMap;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Selection;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Manager.ToolManager;
import fr.Shiranuit.LogForJustice.PlayerData.NBTPlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.Utils.Time;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventsOther {
	public static HashMap<String, Integer>tickParticle=new HashMap<String, Integer>();
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if (e.player != null) {
			if (Main.VanishedPlayers.contains(e.player.getName())) {
				e.player.setInvisible(true);
			}

			if (RankManager.invulnerable && RankManager.rankName(e.player).equals("default")) {
				e.player.getFoodStats().setFoodLevel(20);
				e.player.setHealth(e.player.getMaxHealth());
			}
			
			if (Util.isOp(e.player) && ToolManager.showRegion) {
				if (tickParticle.containsKey(e.player.getName())) {
					String mainHand = Util.nameFromItemStack(e.player.getHeldItemMainhand());
					String offHand = Util.nameFromItemStack(e.player.getHeldItemOffhand());
					if (mainHand.equals(ToolManager.ShowRegionToolID) || offHand.equals(ToolManager.ShowRegionToolID)) {
						int ticks = tickParticle.get(e.player.getName());
						tickParticle.put(e.player.getName(), ++ticks);
						if (ticks > 80){
							tickParticle.put(e.player.getName(), 0);
							Area area = AreaManager.getArea(e.player);
							if (area != null) {
								drawAreaWithParticles(area, e.player, EnumParticleTypes.BARRIER, 1f);
							}
						}
					} else if (mainHand.equals(ToolManager.RegionToolID)) {
						int ticks = tickParticle.get(e.player.getName());
						tickParticle.put(e.player.getName(), ++ticks);
						if (ticks > 80){
							tickParticle.put(e.player.getName(), 0);
							Selection s = AreaManager.getSelection(e.player);
							if (s != null && s.isSet()) {
								drawAreaWithParticles(s.toArea(e.player.dimension), e.player, EnumParticleTypes.BARRIER, 1f);
							}
						}
					} else {
						tickParticle.put(e.player.getName(), 81);
					}
				} else {
					tickParticle.put(e.player.getName(), 0);
				}
			}
		}
	}
	
	private void drawAreaWithParticles(Area area, EntityPlayer player, EnumParticleTypes particle, float speed) {
		boolean longDistance = true;
		for (int x=area.x; x<area.dx; x++) {
			SPacketParticles spacketparticles = new SPacketParticles(particle, longDistance, (float)x+0.5f, (float)area.y+0.5f, (float)area.z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, x+0.5f, area.y+0.5f, area.z+0.5f, spacketparticles);
			spacketparticles = new SPacketParticles(particle, longDistance, (float)x+0.5f, (float)area.dy+0.5f, (float)area.z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, x+0.5f, area.dy+0.5, area.z+0.5f, spacketparticles);
			spacketparticles = new SPacketParticles(particle, longDistance, (float)x+0.5f, (float)area.y+0.5f, (float)area.dz+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, x+0.5f, area.y+0.5f, area.dz+0.5f, spacketparticles);
			spacketparticles = new SPacketParticles(particle, longDistance, (float)x+0.5f, (float)area.dy+0.5f, (float)area.dz+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, x+0.5f, area.dy+0.5, area.dz+0.5f, spacketparticles);
		}
		
		for (int z=area.z; z<area.dz; z++) {
			SPacketParticles spacketparticles = new SPacketParticles(particle, longDistance, (float)area.x+0.5f, (float)area.y+0.5f, (float)z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.x+0.5f, area.y+0.5f, z+0.5f, spacketparticles);
			 spacketparticles = new SPacketParticles(particle, longDistance, (float)area.x+0.5f, (float)area.dy+0.5f, (float)z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.x+0.5f, area.dy, z+0.5f, spacketparticles);
			spacketparticles = new SPacketParticles(particle, longDistance, (float)area.dx+0.5f, (float)area.y+0.5f, (float)z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.dx+0.5f, area.y+0.5, z+0.5f, spacketparticles);
			 spacketparticles = new SPacketParticles(particle, longDistance, (float)area.dx+0.5f, (float)area.dy+0.5f, (float)z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
				sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.dx+0.5f, area.dy+0.5, z+0.5f, spacketparticles);
		}
		
		for (int y=area.y; y<area.dy+1; y++) {
			SPacketParticles spacketparticles = new SPacketParticles(particle, longDistance, (float)area.x+0.5f, (float)y, (float)area.z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.x+0.5f, y+0.5f, area.z+0.5f, spacketparticles);
			 spacketparticles = new SPacketParticles(particle, longDistance, (float)area.dx+0.5f, (float)y+0.5f, (float)area.z+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.dx+0.5f, y+0.5f, area.z+0.5f, spacketparticles);
			spacketparticles = new SPacketParticles(particle, longDistance, (float)area.x+0.5f, (float)y+0.5f, (float)area.dz+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.x+0.5f, y+0.5f, area.dz+0.5f, spacketparticles);
			 spacketparticles = new SPacketParticles(particle, longDistance, (float)area.dx+0.5f, (float)y+0.5f, (float)area.dz+0.5f, (float)0, (float)0, (float)0, (float)speed, 1, 0);
			sendPacketWithinDistance((EntityPlayerMP)player, longDistance, area.dx+0.5f, y+0.5f, area.dz+0.5f, spacketparticles);
		}
	}
	
    private void sendPacketWithinDistance(EntityPlayerMP player, boolean longDistance, double x, double y, double z, Packet<?> packetIn)
    {
        BlockPos blockpos = player.getPosition();
        double d0 = blockpos.distanceSq(x, y, z);

        if (d0 <= 1024.0D || longDistance && d0 <= 262144.0D)
        {
            player.connection.sendPacket(packetIn);
        }
    }
	
	
	public int  tick = 0;
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent e) {
		if (Main.HideTabPacket) {
			tick++;
			if (tick >= Main.SSend*40) {
				tick=0;
				for (String name : Main.VanishedPlayers) {
					EntityPlayer p = Util.playerByName(name);
					if (p != null) {
						Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER,  (EntityPlayerMP)p ));
					}
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public void NetHandlerLoggin(ServerConnectionFromClientEvent e) {
		NetHandlerPlayServer netHandler = (NetHandlerPlayServer) e.getHandler();
		EntityPlayer player = netHandler.player;
		PlayerData pdata = PlayerManager.getPlayerData(player);
		if (pdata != null && pdata.tempban != null) {
			long now = new Date().getTime();
			long unban = pdata.tempban.time.getTime();
			long remaining = unban - now;
			if (remaining > 0) {
				Time time = new Time(remaining);
				netHandler.disconnect(new TextComponentString("Remaining time before being unbanned "+time.getDays()+" days, "+time.getHours()+" hours, "+time.getMinutes()+" minutes, "+time.getSeconds()+" seconds"));
			} else {
				pdata.tempban = null;
				pdata.save();
			}
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
		PlayerManager.nbtplayerdata.put(e.player.getName(), new NBTPlayerData(e.player));
		PlayerManager.add(e.player);
		PlayerData pdata = PlayerManager.getPlayerData(e.player);
		if (pdata != null) {
			pdata.lastPos = e.player.getPosition();
			pdata.lastSeen = new Date();
			pdata.bedPos = e.player.getBedLocation();
			pdata.save();
		}
		
		EntityPlayerMP mplayer = (EntityPlayerMP)e.player;
	
	
		
		if (!RankManager.isPlayerRanked(e.player)) {
			boolean result = RankManager.setRank(e.player, "default");
			TextComponentString txt = new TextComponentString(RankManager.connectionMessage.replace("&", "\247").replace("{PLAYERNAME}", e.player.getName()));
			e.player.sendMessage(txt);
		}
		
		if (Main.HideTabPacket) {
			if (Main.VanishedPlayers != null && Main.VanishedPlayers.size() > 0 && Main.mcserver != null) {
				for (String name : Main.VanishedPlayers) {
					EntityPlayer player = Util.playerByName(name);
					if (player != null) {
						Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, (EntityPlayerMP)player));
					}
				}
			}
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
		PlayerData pdata = PlayerManager.getPlayerData(e.player);
		if (pdata != null) {
			pdata.lastPos = e.player.getPosition();
			pdata.lastSeen = new Date();
			pdata.bedPos = e.player.getBedLocation();
			pdata.save();
		}
	}
}
