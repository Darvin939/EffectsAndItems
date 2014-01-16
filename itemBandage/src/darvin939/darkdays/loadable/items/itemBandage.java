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
import darvin939.DarkDays.Configuration.Config.Nodes;
import darvin939.DarkDays.Configuration.PlayerConfig;
import darvin939.DarkDays.Listeners.TagAPIListener;
import darvin939.DarkDays.Loadable.Item;
import darvin939.DarkDays.Loadable.LiteConfig;
import darvin939.DarkDays.Players.Memory.PlayerInfo;
import darvin939.DarkDays.Utils.Util;
import darvin939.DeprecAPI.ItemAPI;

public class itemBandage extends Item {
	private LiteConfig cfg;
	private FileConfiguration section;
	private int bandage_health;
	private String mess3;
	private String mess2;
	private String mess1;
	private String mess4;

	public itemBandage(DarkDays plugin) {
		super(plugin, "Bandage");
		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();

		setDepend(section.getString("Depend", "Bleeding"));
		setItem(section.getInt("Id", 339));
		bandage_health = section.getInt("Restore_health", 8);
		
		// setMessage("item_use_water", "You drink some water");

		mess1 = section.getString("Message_1", "You were bandaged by &2");
		mess2 = section.getString("Message_2", "You bandaged &2");
		mess3 = section.getString("Message_3", "You bandaged yourself");
		mess4 = section.getString("Message_4", " is healthy");
		

		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Depend", getDepend());
		section.set("Id", getItem());
		section.set("Restore_health", bandage_health);
		cfg.save();
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
			if ((boolean) Config.getPC().getData(event.getPlayer(), PlayerConfig.SPAWNED)) {
				if (((event.getRightClicked() instanceof Player)) && (ItemAPI.get(event.getPlayer().getItemInHand().getType()).id() == getItem())) {
					Player e = (Player) event.getRightClicked();
					Player p = event.getPlayer();
					if (!DarkDays.getEffectManager().isEffect(getDepend())) {
						Util.PrintMSG(p, "cmd_effectnf", getDepend());
						return;
					}
					if (PlayerInfo.isPlaying(p) && PlayerInfo.isPlaying(e)) {
						if (DarkDays.getEffectManager().isEffect(e, getDepend())) {
							if (e.getHealth() < 20) {
								if (e.getHealth() <= 20 - bandage_health)
									e.setHealth(e.getHealth() + bandage_health);
								else {
									e.setHealth(20);
								}
								if (p.getItemInHand().getAmount() > 1)
									p.setItemInHand(new ItemStack(ItemAPI.get(getItem()).type(), p.getItemInHand().getAmount() - 1));
								else {
									p.setItemInHand(new ItemStack(Material.AIR, 0));
								}
								PlayerInfo.addPlayerHeal(p);
								if (Nodes.coloured_tegs.getBoolean()) {
									TagAPIListener.refreshPlayer(p);
								}
								DarkDays.getEffectManager().cancelEffect(e, getDepend());
								Util.Print(e, mess1 + p.getName());
								Util.Print(p, mess2 + e.getName());
							}
						} else
							Util.Print(p, e.getName() + mess4);
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteract(PlayerInteractEvent event) {
			Player p = event.getPlayer();
			if (PlayerInfo.isPlaying(p)) {
				if (ItemAPI.get(event.getPlayer().getItemInHand().getType()).id() == getItem()) {
					if (!DarkDays.getEffectManager().isEffect(getDepend())) {
						Util.PrintMSG(p, "cmd_effectnf", getDepend());
						return;
					}
					if (DarkDays.getEffectManager().isEffect(p, getDepend())) {
						if (p.getHealth() < 20) {
							if (p.getHealth() <= 20 - bandage_health)
								p.setHealth(p.getHealth() + bandage_health);
							else {
								p.setHealth(20);
							}
							if (p.getItemInHand().getAmount() > 1)
								p.setItemInHand(new ItemStack(ItemAPI.get(getItem()).type(), p.getItemInHand().getAmount() - 1));
							else {
								p.setItemInHand(new ItemStack(Material.AIR, 0));
							}
							DarkDays.getEffectManager().cancelEffect(p, getDepend());
							Util.Print(p, mess3);
						}
					}
				}
			}
		}
	}
}
