package darvin939.darkdays.loadable.effects;

import org.bukkit.Bukkit;
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
import darvin939.DarkDays.Loadable.EffectManager;

public class effectBleeding extends Effect {

	public effectBleeding(DarkDays plugin) {
		super(plugin, "Bleeding");
		setPercent(30);
		setDelay(200);
		setTime(30);
		Bukkit.getServer().getPluginManager().registerEvents(new EffectListener(this), plugin);
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
						addEffect(p, run(p));
					}
				}
			}
		}
	}

	public Integer run(final Player p) {
		return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if (EffectManager.isEffect(p, name) != null) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName("wither"), getTime(), 1));
					p.setHealth(p.getHealth() - 2);
				}

			}
		}, getDelay(), getDelay());
	}
}
