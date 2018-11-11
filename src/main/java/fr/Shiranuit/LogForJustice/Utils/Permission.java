package fr.Shiranuit.LogForJustice.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import fr.Shiranuit.LogForJustice.Manager.RankManager;
import net.minecraft.entity.player.EntityPlayer;

public class Permission {
	public HashMap<String, Integer> permission = new HashMap<String, Integer>();
	private int defaultValue = 0;
	public Permission(int defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public Permission(int defaultValue, String ...data) {
		this.defaultValue = defaultValue;
		process(ArrayConverter.convert(data));
	}
	public Permission() {
		this.defaultValue = 0;
	}
	
	public int defaultValue() {
		return this.defaultValue;
	}
	
	private void processPermission(String cmd) {
		String[] data = cmd.split("[:]");
		if (data.length >= 2) {
			try {
				String cmdName = data[0].toString();
				int cmdLvl = Integer.valueOf(data[1]).intValue();
				permission.put(cmdName, cmdLvl);
			} catch (Exception e) {
				String cmdName = data[0].toString();
				permission.put(cmdName, 0);
			}
		} else {
			permission.put(cmd, 0);
		}
	}
	
	public Permission process(ArrayList<String> lst) {
		permission.clear();
		for (String perm :  lst) {
			processPermission(perm);
		}
		return this;
	}
	
	public void apply(HashMap<String, Integer> map) {
		for (String key : this.permission.keySet()) {
			map.put(key, this.permission.get(key));
		}
	}
	
	public void clear() {
		this.permission.clear();
	}
	
	public boolean existPermission(String name) {
		return permission.containsKey(name);
	}
	
	public int permissionPower(String name) {
		if (existPermission(name)) {
			return permission.get(name);
		}
		return this.defaultValue;
	}
	
	public boolean hasPermission(String name, int permLevel) {
		if (existPermission(name)) {
			if (permissionPower(name) <= permLevel) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public String[] toArray() {
		String[] str = new String[permission.size()];
		int index = 0;
		for (String key : permission.keySet()) {
			str[index] = key+":"+permission.get(key);
			index++;
		}
		return str;
	}
}
