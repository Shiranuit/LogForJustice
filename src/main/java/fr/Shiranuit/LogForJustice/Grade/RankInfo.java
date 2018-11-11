package fr.Shiranuit.LogForJustice.Grade;

import java.io.File;
import java.util.ArrayList;

import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import net.minecraftforge.common.config.Configuration;

public class RankInfo {
	public String displayname= "";
	public int power = 0;
	public ArrayList<String> members;
	public int homenumber = 0;
	public ArrayList<String> cmd;
	public Configuration cfg;
	
	public RankInfo(String displayname, int level, ArrayList<String> playernames, Configuration cfg, int homenumber, ArrayList<String> cmd) {
		this.displayname = displayname;
		this.power = level;
		this.members = playernames;
		this.cfg = cfg;
		this.homenumber = homenumber;
		this.cmd = cmd;
	}
	
	public void save() {
		File f = cfg.getConfigFile();
		if (f.exists()) {
			cfg.getCategory("rankinfo").get("members").set(ArrayConverter.convert(this.members));
			cfg.getCategory("rankinfo").get("displayname").set(this.displayname);
			cfg.getCategory("rankinfo").get("level").set(this.power);
			cfg.getCategory("rankinfo").get("maxhomes").set(this.homenumber);
			cfg.getCategory("rankinfo").get("commands").set(ArrayConverter.convert(this.cmd));
		} else {
			cfg.get("rankinfo", "members", ArrayConverter.convert(this.members), "List of players that are members of this rank");
			cfg.get("rankinfo", "displayname", this.displayname, "name display before playernames");
			cfg.get("rankinfo", "level", this.power, "power rank of the members");
			cfg.get("rankinfo", "maxhomes", this.homenumber, "Max homes players member of this rank can have");
			cfg.get("rankinfo", "commands", ArrayConverter.convert(this.cmd), "Add or Remove commands to ranks");
		}
		this.cfg.save();
	}
}
