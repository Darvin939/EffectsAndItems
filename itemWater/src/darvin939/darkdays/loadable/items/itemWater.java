package darvin939.darkdays.loadable.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Tasks;
import darvin939.DarkDays.Configuration.Config;
import darvin939.DarkDays.Loadable.Item;
import darvin939.DarkDays.Players.Memory.PlayerInfo;

public class itemWater extends Item {

	public itemWater(DarkDays plugin) {

		super(plugin, "wBottle");
		setMessage("item_drink_water");

		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.LOW)
		public void wBottle(PlayerInteractEvent event) {
			Player p = event.getPlayer();
			if (PlayerInfo.isPlaying(p)) {
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

	public void drink(final Player p, final int hs) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p.getInventory().getItem(hs).getType() == Material.GLASS_BOTTLE) {
					Tasks.player_hunger.put(p, Integer.valueOf(209999));
					Config.FGU.PrintPxMsg(p, Config.FGU.MSG(getMessage()));
				}
			}
		}, 35);
	}
}
