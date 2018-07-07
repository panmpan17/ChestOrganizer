package com.gmail.panmpan.ChestOrganizer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class ChestOrganizer extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event) {
		if (event.getInventory().getHolder() instanceof Chest || event.getInventory().getHolder() instanceof DoubleChest) {
			Player player = (Player) event.getPlayer();
			ItemStack mainHandItem = player.getInventory().getItemInMainHand();
			
//			getLogger().info();
			if (mainHandItem.getType() == Material.STICK) {
				Inventory inventory = event.getInventory();
				List<ItemStack> inventoryList = new ArrayList<ItemStack>();
				
				for (ItemStack item: inventory) {
					try {
						item.getType();
						inventoryList.add(item);
					}
					catch (Exception e) {}
				}
				
				inventoryList = this.sortInventory(inventoryList);
				inventory.clear();
				
				for (ItemStack item: inventoryList) {
					inventory.addItem(item);
				}
				
				player.sendMessage(ChatColor.GOLD + "MAGIC !!!");
				event.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private List<ItemStack> sortInventory(List<ItemStack> inventory) {
		if (inventory.size() == 1) {
			return inventory;
		}
		if (inventory.size() == 2) {
			if (inventory.get(0).getTypeId() > inventory.get(1).getTypeId()) {
				ItemStack removedItem = inventory.remove(0);
				inventory.add(removedItem);
			}
			return inventory;
		}
		
		int splitIndex = (int) inventory.size() / 2;
		
		List<ItemStack> inventory1 = new ArrayList<ItemStack>(this.sortInventory(inventory.subList(0, splitIndex)));
		List<ItemStack> inventory2 = new ArrayList<ItemStack>(this.sortInventory(inventory.subList(splitIndex, inventory.size())));
		List<ItemStack> newInventory = (List<ItemStack>) new ArrayList<ItemStack>(); 

		int index1 = 0;
		int index2 = 0;
		
		for (int i=0;i<inventory.size();i++) {
			if (index1 == inventory1.size()) {
				newInventory.add(inventory2.get(index2));
				index2++;
			}
			else if (index2 == inventory2.size()) {
				newInventory.add(inventory1.get(index1));
				index1++;
			}
			else if (inventory1.get(index1).getTypeId() < inventory2.get(index2).getTypeId()) {
				newInventory.add(inventory1.get(index1));
				index1++;
			}
			else {
				newInventory.add(inventory2.get(index2));
				index2++;
			}
		}
		
		return newInventory;
	}
}
