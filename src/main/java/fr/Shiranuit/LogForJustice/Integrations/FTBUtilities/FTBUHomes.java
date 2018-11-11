package fr.Shiranuit.LogForJustice.Integrations.FTBUtilities;

import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesPlayerData;
import com.mojang.authlib.GameProfile;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;

public class FTBUHomes {

	public static void retrieveHomes() {
		for (String username : Main.mcserver.getPlayerProfileCache().getUsernames()) {
			GameProfile profile = Main.mcserver.getPlayerProfileCache().getGameProfileForUsername(username);
			ForgePlayer p = Universe.get().getPlayer(profile);
			if (p!=null) {
				FTBUtilitiesPlayerData data = FTBUtilitiesPlayerData.get(p);
				if (data != null) {
					for (String homename : data.homes.list()) {
						BlockDimPos pos = data.homes.get(homename);
						HomeManager.setHome(profile.getId().toString(), homename, new Position(pos.posX, pos.posY, pos.posZ, pos.dim));
					}
				}
			}
		}
	}
}
