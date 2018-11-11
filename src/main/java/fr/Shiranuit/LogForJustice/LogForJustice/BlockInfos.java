package fr.Shiranuit.LogForJustice.LogForJustice;

import net.minecraft.item.ItemStack;


public class BlockInfos {
	public int id = 0;
	public int meta = 0;
	public int itemID = 0;
	public int itemMeta = 0;
	public String playername = "";
	public String date="";
	public EnumPlayerAction action;
	public BlockInfos(EnumPlayerAction action, String playername, int id, int meta, int itemID, int itemMeta, String date) {
		this.playername = playername;
		this.id = id;
		this.meta = meta;
		this.itemID = itemID;
		this.itemMeta = itemMeta;
		this.date = date;
		this.action=action;
	}
}
