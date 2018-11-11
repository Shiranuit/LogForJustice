package fr.Shiranuit.LogForJustice.Integrations.Morph.Events;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import me.ichun.mods.morph.api.MorphApi;
import me.ichun.mods.morph.api.event.MorphAcquiredEvent;
import me.ichun.mods.morph.api.event.MorphEvent;
import me.ichun.mods.morph.common.Morph;
import me.ichun.mods.morph.common.handler.PlayerMorphHandler;
import me.ichun.mods.morph.common.morph.MorphInfo;
import me.ichun.mods.morph.common.morph.MorphState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MorphEvents {
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onMorph(MorphEvent e) {
		if (Main.enabled) {
			Area zone = AreaManager.getArea(e.getEntityPlayer());
			Flag flag = AreaManager.getFlag(zone, "can_morph");
			if (!flag.isNull()) {
				if (flag.isEqual(false) && !(AreaManager.hasBypass(e.getEntityPlayer()))) {
					e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to morph in this area"));
					e.setCanceled(true);
					return;	
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onAquireMorph(MorphAcquiredEvent e) {
		if (Main.enabled) {
			Area zone = AreaManager.getArea(e.getEntityPlayer());
			Flag flag = AreaManager.getFlag(zone, "can_aquire_morph");
			if (!flag.isNull()) {
				if (flag.isEqual(false) && !(AreaManager.hasBypass(e.getEntityPlayer()))) {
					e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to aquire morph in this area"));
					e.setCanceled(true);
					return;	
				}
			}
		}
	}
}
