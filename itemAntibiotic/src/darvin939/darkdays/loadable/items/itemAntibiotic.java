package darvin939.darkdays.loadable.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Configuration.Config;
import darvin939.DarkDays.Configuration.PlayerConfig;
import darvin939.DarkDays.Loadable.Item;
import darvin939.DarkDays.Loadable.LiteConfig;
import darvin939.DarkDays.Players.Memory.PlayerInfo;
import darvin939.DarkDays.Utils.Util;

public class itemAntibiotic extends Item {
	private LiteConfig cfg;
	private FileConfiguration section;

	public itemAntibiotic(DarkDays plugin) {
		super(plugin, "Antibiotic");
		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();

		setDepend(section.getString("Depend", "Poison"));
		setItem(section.getInt("Id", 370));

		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Depend", getDepend());
		section.set("Id", getItem());
		cfg.save();
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
			if ((boolean) Config.getPC().getData(event.getPlayer(), PlayerConfig.SPAWNED)) {
				if (((event.getRightClicked() instanceof Player)) && (event.getPlayer().getItemInHand().getTypeId() == getItem())) {
					Player e = (Player) event.getRightClicked();
					Player p = event.getPlayer();
					if (!DarkDays.getEffectManager().isEffect(getDepend())) {
						Util.PrintPx(p, "Effect &2" + getDepend() + " &fnot found on the server!");
						return;
					}
					if (PlayerInfo.isPlaying(p) && PlayerInfo.isPlaying(e)) {
						if (DarkDays.getEffectManager().isEffect(e, getDepend())) {
							if (p.getItemInHand().getAmount() > 1)
								p.setItemInHand(new ItemStack(Material.getMaterial(getItem()), p.getItemInHand().getAmount() - 1));
							else {
								p.setItemInHand(new ItemStack(Material.AIR, 0));
							}
							DarkDays.getEffectManager().cancelEffect(e, getDepend());
							Util.Print(e, "You were given an antibiotic from &2" + p.getName());
							Util.Print(p, "You have given an antibiotic to &2" + e.getName());
						} else
							Util.Print(p, e.getName() + " is healthy");
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteract(PlayerInteractEvent event) {
			Player p = event.getPlayer();
			if (PlayerInfo.isPlaying(p)) {
				if (event.getPlayer().getItemInHand().getTypeId() == getItem()) {
					if (!DarkDays.getEffectManager().isEffect(getDepend())) {
						Util.PrintPx(p, "Effect &2" + getDepend() + " &fnot found on the server!");
						return;
					}
					if (DarkDays.getEffectManager().isEffect(p, getDepend())) {
						if (p.getItemInHand().getAmount() > 1)
							p.setItemInHand(new ItemStack(Material.getMaterial(getItem()), p.getItemInHand().getAmount() - 1));
						else {
							p.setItemInHand(new ItemStack(Material.AIR, 0));
						}
						DarkDays.getEffectManager().cancelEffect(p, getDepend());
						Util.Print(p, "You used an antibiotic, you feel much better");
					}
				}
			}
		}
	}
}
