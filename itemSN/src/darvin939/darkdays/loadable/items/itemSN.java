package darvin939.darkdays.loadable.items;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Loadable.Item;
import darvin939.DarkDays.Loadable.LiteConfig;
import darvin939.DarkDays.Players.Memory.PlayerInfo;

public class itemSN extends Item {

	private LiteConfig cfg;
	private FileConfiguration section;

	final int delay1;
	final int delay2;

	public itemSN(DarkDays plugin) {

		super(plugin, "Syntetic Narcotic");
		 // setMessage("item_use_sn", "You have used synthetic drug");

		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();
		
		setMessage("item_use_sn", section.getString("Message","You have used synthetic drug"));

		setItem(section.getInt("Id", 352));
		delay1 = section.getInt("SpeedDelay", 400);
		delay2 = section.getInt("BlindnessDelay", 200);

		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Id", getItem());
		section.set("SpeedDelay", 400);
		section.set("BlindnessDelay", 200);
		section.set("Message", section.getString("Message", "You have used synthetic drug"));
		cfg.save();
	}

	public class ItemListener implements Listener {

		public ItemListener() {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteract(PlayerInteractEvent event) {
			final Player p = event.getPlayer();
			if (PlayerInfo.isPlaying(p)) {
				if (p.getItemInHand().getTypeId() == getItem()) {
					if (p.hasPotionEffect(PotionEffectType.getByName("speed")))
						return;
					sendMessage(p);
					minusOne(p);
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName("speed"), delay1, 1));
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							p.addPotionEffect(new PotionEffect(PotionEffectType.getByName("Blindness"), delay2, 1));
						}
					}, delay1);

				}
			}
		}
	}
}
