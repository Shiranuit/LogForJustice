package fr.Shiranuit.LogForJustice.Utils;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ModLoaderUtil {
	public static Object getModObjectByName(String modName) {
		for (String name : Loader.instance().getIndexedModList().keySet()) {
			if(name.toLowerCase().equals(modName.toLowerCase())) {
				ModContainer modcontainer = Loader.instance().getIndexedModList().get(modName);
				return modcontainer.getMod();
			}
		}
		return null;
	}
}
