package minecraft.economy.zocker.pro.listener;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.event.ZockerDataInitializeEvent;
import minecraft.economy.zocker.pro.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZockerDataInitializeListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onZockerDataInitialize(ZockerDataInitializeEvent e) {
		Zocker zocker = e.getZocker();
		zocker.hasValueAsync(Main.ECONOMY_DATABASE_TABLE, "uuid", "uuid", zocker.getPlayer().getUniqueId().toString()).thenApplyAsync(aBoolean -> {
			if (aBoolean) return true;

			zocker.insert(Main.ECONOMY_DATABASE_TABLE,
				new String[]{"uuid", "pocket"},
				new Object[]{zocker.getPlayer().getUniqueId().toString(), Main.ECONOMY_CONFIG.getDouble("economy.balance.start")});

			return aBoolean;
		});
	}
}
