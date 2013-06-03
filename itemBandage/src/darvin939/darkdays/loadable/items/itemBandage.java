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

public class itemBandage extends Item {
	private LiteConfig cfg;
	private FileConfiguration section;
	private int bandage_id;
	private int bandage_health;

	public itemBandage(DarkDays plugin) {
		super(plugin, "Bandage");
		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();

		setDepend(section.getString("Depend", "Bleeding"));
		bandage_id = section.getInt("Bandage_id", 339);
		bandage_health = section.getInt("Bandage_health", 8);

		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Depend", getDepend());
		section.set("Bandage_id", bandage_id);
		section.set("Bandage_health", bandage_health);
		cfg.save();
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
			if ((boolean) Config.getPC().getData(event.getPlayer(), PlayerConfig.SPAWNED)) {
				if (((event.getRightClicked() instanceof Player)) && (event.getPlayer().getItemInHand().getTypeId() == bandage_id)) {
					Player e = (Player) event.getRightClicked();
					Player p = event.getPlayer();
					if (!DarkDays.getEffectManager().isEffect(getDepend())) {
						Util.PrintPx(p, "Effect &2" + getDepend() + " &fnot found on the server!");
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
									p.setItemInHand(new ItemStack(Material.getMaterial(bandage_id), p.getItemInHand().getAmount() - 1));
								else {
									p.setItemInHand(new ItemStack(Material.AIR, 0));
								}
								PlayerInfo.addPlayerHeal(p);
								if (Nodes.coloured_tegs.getBoolean()) {
									TagAPIListener.refreshPlayer(p);
								}
								DarkDays.getEffectManager().cancelEffect(e, getDepend());
								Util.Print(e, "You were bandaged by " + p.getName());
								Util.Print(p, "You bandaged " + e.getName());
							}
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
				Integer bandage_id = Nodes.bandage_id.getInteger();
				Integer bandage_health = Nodes.bandage_health.getInteger();
				if (event.getPlayer().getItemInHand().getTypeId() == bandage_id) {
					if (DarkDays.getEffectManager().isEffect(p, getDepend())) {
						if (p.getHealth() < 20) {
							if (p.getHealth() <= 20 - bandage_health)
								p.setHealth(p.getHealth() + bandage_health);
							else {
								p.setHealth(20);
							}
							if (p.getItemInHand().getAmount() > 1)
								p.setItemInHand(new ItemStack(Material.getMaterial(bandage_id), p.getItemInHand().getAmount() - 1));
							else {
								p.setItemInHand(new ItemStack(Material.AIR, 0));
							}
							DarkDays.getEffectManager().cancelEffect(p, getDepend());
							Util.Print(p, "You bandaged yourself");
						}
					}
				}
			}
		}
	}
}
