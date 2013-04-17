package darvin939.darkdays.item;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Players.Item;

public class itemWater extends Item {
	
	public itemWater(DarkDays plugin) {
		
		super(plugin, "wBottle");
		setDescription("Water Bottle");
		setDrinkMSG("item_drink_water");
		setMaterial("GLASS_BOTTLE");
		
		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.LOW)
		public void wBottle(PlayerInteractEvent event) {
			Player p = event.getPlayer();
			Potion wBottle = new Potion(PotionType.WATER);
			ItemStack temp = new ItemStack(wBottle.toItemStack(1));
			ItemStack iIH = p.getItemInHand();
			if ((iIH.getType().equals(temp.getType())) && (iIH.getData().equals(temp.getData())) && (iIH.getDurability() == temp.getDurability())) {
				int hs = p.getInventory().getHeldItemSlot();
				drink(p, hs);
			}
		}
	}
}
