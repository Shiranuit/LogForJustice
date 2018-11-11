package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import net.minecraftforge.common.config.Configuration;

public class ConfigManager {
	public static String modLogDir;
	
	public static Configuration EssentialsConfig;
	public static void sync() {
		if (EssentialsConfig != null) {
			EssentialsConfig.load();
		} else {
			EssentialsConfig = new Configuration(new File(modLogDir + File.separatorChar + "Essentials.cfg"));
		}
		RankManager.commandsLevel.process(ArrayConverter.convert(EssentialsConfig.get("grade", "commandslevel", new String[] {}, "List of commands power").getStringList()));
		RankManager.permissionsLevel.process(ArrayConverter.convert(EssentialsConfig.get("grade", "permissionslevel", new String[] {"place.*:0","break.*:0","use.*:0","interact.*:0","itempickup.*:0","attack-entity.*:0","interact-entity.*:0"}, "List of permissions power").getStringList()));
		AreaManager.processCurrencies(ArrayConverter.convert(EssentialsConfig.get("money", "items-value", new String[] {"minecraft:diamond/0=100","minecraft:gold_ingot/0=50","minecraft:iron_ingot/0=25"}, "Value in money of items").getStringList()));
		Main.VanishedPlayers = ArrayConverter.convert(EssentialsConfig.get("op", "vanished-players", new String[] {}, "List of vanished users").getStringList());
		LogForJusticeManager.enabled = EssentialsConfig.get("LogForJustice", "block-logs", true, "Define if LogForJustice has to log blocks").getBoolean();
		Main.HideTabPacket = EssentialsConfig.get("Vanish", "hide-tab", true,"Define if the vanished players does not show up in the tab-list").getBoolean();
		Main.SSend = EssentialsConfig.get("Vanish", "hide-tab-second", 1f, "Define the time before sending the packet to hide vanished players in the tab-list").getDouble();
		Main.enabled = EssentialsConfig.get("LogForJustice", "enabled", true, "Define if LogForJustice is enabled").getBoolean();
		LogForJusticeManager.swapSize = EssentialsConfig.get("LogForJustice", "swapsize", 50, "Size of logs before it get to the folder oldLogs").getInt();
		ToolManager.RegionToolID = EssentialsConfig.get("Tools", "region_tool", "minecraft:golden_shovel", "Tool ID for selecting region").getString();
		ToolManager.LogForJusticeToolID = EssentialsConfig.get("Tools", "block_logs_Tool", "minecraft:golden_pickaxe", "Tool ID for showing logs of a block").getString();
		ToolManager.ShowRegionToolID = EssentialsConfig.get("Tools", "region_border_tool", "minecraft:golden_axe", "Tool ID for showing region border").getString();
		ToolManager.showRegion = EssentialsConfig.get("Tools", "show_region", true, "Enable/Disable showing region or selection").getBoolean();
		PlayerManager.requestExpireTime = EssentialsConfig.get("TPA", "expiration-time", 30, "Time before TPA request expire").getInt();
		HomeManager.homeNumber = EssentialsConfig.get("homes", "max", 5, "Maximum number of Homes that a player can have").getInt();
		EssentialsConfig.save();
	}
}
