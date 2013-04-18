package darvin939.darkdays.loadable.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Configuration.Config;
import darvin939.DarkDays.Configuration.Config.Nodes;
import darvin939.DarkDays.Configuration.PC;
import darvin939.DarkDays.Loadable.EffectManager;
import darvin939.DarkDays.Loadable.Item;
import darvin939.DarkDays.Players.Memory.PlayerInfo;

public class itemBandage extends Item {

	public itemBandage(DarkDays plugin) {

		super(plugin, "Bandage");
		setDepend("Bleeding");
		Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
	}

	public class ItemListener implements Listener {

		public ItemListener(Item effect) {
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void milk(PlayerInteractEntityEvent event) {
			if ((boolean) Config.getPC().getData(event.getPlayer(), PC.SPAWNED)) {
				Integer bandage_id = Nodes.bandage_id.getInteger();
				Integer bandage_health = Nodes.bandage_health.getInteger();
				if (((event.getRightClicked() instanceof Player)) && (event.getPlayer().getItemInHand().getTypeId() == bandage_id)) {
					Player e = (Player) event.getRightClicked();
					Player p = event.getPlayer();
					if (PlayerInfo.isPlaying(p) && PlayerInfo.isPlaying(e)) {
						if (EffectManager.isEffect(e, getDepend()) != null) {
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
									TagAPI.refreshPlayer(p);
								}
								DarkDays.getEffectManager().cancelEffect(e, getDepend());
								e.sendMessage("You were bandaged by " + p.getName());
								p.sendMessage("You bandaged " + e.getName());
							}
						} else
							p.sendMessage(e.getName() + " is healthy");
					}
				}
			}
		}
	}
}
