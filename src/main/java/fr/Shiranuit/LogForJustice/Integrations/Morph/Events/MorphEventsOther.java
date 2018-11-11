package fr.Shiranuit.LogForJustice.Integrations.Morph.Events;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import me.ichun.mods.morph.api.MorphApi;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MorphEventsOther {
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if (Main.enabled) {
			if (MorphApi.getApiImpl().hasMorph(e.player.getName(), e.side)) {
				EntityLivingBase ent = MorphApi.getApiImpl().getMorphEntity(e.player.getEntityWorld(), e.player.getName(), e.side);
				if (!(ent instanceof EntityPlayer)) {
					
					Area zone = AreaManager.getArea(e.player);
					Flag flag = AreaManager.getFlag(zone, "force_unmorph");
					if (!flag.isNull()) {
						if (flag.isEqual(true) && !(AreaManager.hasBypass(e.player))) {
							MorphApi.getApiImpl().forceDemorph((EntityPlayerMP)e.player);
							return;	
						}
					}
				}
			}
		}
	}
}
