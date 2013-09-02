package darvin939.darkdays.loadable.effects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Loadable.Effect;
import darvin939.DarkDays.Loadable.LiteConfig;

public class effectPoison extends Effect {

	private LiteConfig cfg;
	private FileConfiguration section;
	private int amplifier;

	public effectPoison(DarkDays plugin) {
		super(plugin, "Poison");
		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();

		setPercent(section.getInt("Percent", 50));
		setDelay(section.getInt("Delay", 1000));
		setTime(section.getInt("Time", 600));
		amplifier = section.getInt("Amplifier", 0);

		Bukkit.getServer().getPluginManager().registerEvents(new EffectListener(this), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Percent", getPercent());
		section.set("Delay", getDelay());
		section.set("Time", getTime());
		section.set("Amplifier", amplifier);
		cfg.save();
	}

	public class EffectListener implements Listener {

		public EffectListener(Effect effect) {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerInteract(EntityDamageByEntityEvent event) {
			if (event.getEntity() instanceof Player) {
				Player p = (Player) (event.getEntity());
				if (event.getDamager() instanceof Zombie) {
					if (isPercent() && !isEffect(p)) {
						p.sendMessage("Poison");
						addEffect(p, run(p));
					}
				}
			}
		}
	}

	public Integer run(final Player p) {
		return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if (DarkDays.getEffectManager().isEffect(p, name)) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName("poison"), getTime(), amplifier));
				}

			}
		}, 200, getDelay());
	}
}
