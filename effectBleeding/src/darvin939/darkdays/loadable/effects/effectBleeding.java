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

public class effectBleeding extends Effect {

	private LiteConfig cfg;
	private int damage;
	private FileConfiguration section;
	private int amplifier;

	public effectBleeding(DarkDays plugin) {
		super(plugin, "Bleeding");
		cfg = new LiteConfig(plugin, getClass());
		section = cfg.get();

		setPercent(section.getInt("Percent", 10));
		setDelay(section.getInt("Delay", 200));
		setTime(section.getInt("Time", 30));
		damage = section.getInt("Damage", 2);
		amplifier = section.getInt("Amplifier", 1);
		
		setMessage("effect_use_bleeding", section.getString("Message", "Bleeding"));

		Bukkit.getServer().getPluginManager().registerEvents(new EffectListener(this), plugin);
		saveConfig();
	}

	public void saveConfig() {
		section.set("Percent", getPercent());
		section.set("Delay", getDelay());
		section.set("Time", getTime());
		section.set("Damage", damage);
		section.set("Amplifier", amplifier);
		section.set("Message", section.getString("Message", "Bleeding"));
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
						sendMessage(p);
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
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName("wither"), getTime(), amplifier));
					if (p.getHealth() >= damage)
						p.setHealth(p.getHealth() - damage);
					else
						p.setHealth(0);
				}

			}
		}, 150, getDelay());
	}
}
