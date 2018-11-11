package fr.Shiranuit.LogForJustice.Manager;

import java.util.HashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MoneyManager {
    public static double calcMoney(EntityPlayer player) {
    	double count = 0D;
    	for (int i=0; i<player.inventory.getSizeInventory(); i++) {
    		ItemStack stack = player.inventory.getStackInSlot(i);
	    		if (stack != ItemStack.EMPTY) {
	    		ResourceLocation loc = stack.getItem().getRegistryName();
	    		String name = loc.getResourceDomain() + ":" + loc.getResourcePath()+"/"+stack.getItemDamage();
	    		if (AreaManager.currency.containsKey(name)) {
	    			count += AreaManager.currency.get(name) * stack.getCount();
	    		}
    		}
    	}
    	return count;
    }
    
    public static void addItemStack(EntityPlayer player, ItemStack stack) {
    	boolean success = player.inventory.addItemStackToInventory(stack);
    	if (!success) {
    		player.getEntityWorld().spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));
    	}
    }
    
    public static double clear(EntityPlayer player) {
    	double money = 0D;
    	for (int i=0; i<player.inventory.getSizeInventory(); i++) {
    		ItemStack stack = player.inventory.getStackInSlot(i);
	    		if (stack != ItemStack.EMPTY) {
	    		ResourceLocation loc = stack.getItem().getRegistryName();
	    		String name = loc.getResourceDomain() + ":" + loc.getResourcePath()+"/"+stack.getItemDamage();
	    		if (AreaManager.currency.containsKey(name)) {
	    			money += AreaManager.currency.get(name) * stack.getCount();
	    			player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
	    		}
    		}
    	}
    	return money;
    }
    
    public static Double removeIfCan(EntityPlayer player, double amount) {
    	double money = clear(player);
    	double copymoney = money;
    	if (money >= amount) {
    		money -= amount;
    	}
    	add(player,money);
    	if (copymoney >= amount) {
    		return null;
    	}
    	return money;
    }
    
    public static String getName(double maxvalue) {
    	String name = "";
    	double val = 0D;
    	for (String itemname : AreaManager.currency.keySet()) {
    		double value = AreaManager.currency.get(itemname);
    		if (value > val && value < maxvalue) {
    			val = value;
    			name = itemname;
    		}
    	}
    	return name;
    }
    
    public static void set(EntityPlayer player, double amount) {
    	clear(player);
    	add(player, amount);
    }
    
    public static void add(EntityPlayer player, double amount) {
    	HashMap<String, Integer> items = convertMoneyItem(amount);
    	for (String itemname : items.keySet()) {
			int count = items.get(itemname);
			String data[] = itemname.split("/");
			String id = itemname;
			int meta=0;
			if (data.length >= 2) {
				try {
					meta = Integer.valueOf(data[1]);
					id = data[0];
				} catch (Exception e) {
					meta=0;
					id=data[0];
				}
			}
			Item item = Item.getByNameOrId(id);
			
			int fullStack = (count - (int)Math.floor(count % 64)) / 64;
			int restStack = count % 64;
			for (int i=0; i<fullStack;i++) {
				addItemStack(player, new ItemStack(item, 64, meta));
			}
			addItemStack(player, new ItemStack(item, restStack, meta));
		}
    }
    
    public static void remove(EntityPlayer player, double amount) {
    	double money = clear(player);
    	if (money - amount > 0) {
    		add(player, money-amount);
    	}
    }
    
    public static HashMap<String, Integer> convertMoneyItem(double money) {
    	HashMap<String, Integer> items = new HashMap<String, Integer>();
    	String name = getName(Double.MAX_VALUE);
    	double value = AreaManager.currency.get(name);
    	while (money > 0 && !name.equals("")) {
    		while (money-value >= 0) {
        		money -= value;
        		if (items.containsKey(name)) {
        			items.put(name, items.get(name)+1);
        		} else {
        			items.put(name, 1);
        		}
        	}
    		name = getName(value);
    		if (AreaManager.currency.containsKey(name)) {
    			value = AreaManager.currency.get(name);
    		}
    	}
    	return items;
    }
}
