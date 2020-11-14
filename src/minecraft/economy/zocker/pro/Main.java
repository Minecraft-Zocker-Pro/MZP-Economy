package minecraft.economy.zocker.pro;

import minecraft.core.zocker.pro.CorePlugin;
import minecraft.core.zocker.pro.compatibility.ServerVersion;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.storage.StorageManager;
import minecraft.economy.zocker.pro.command.BalanceInfoCommand;
import minecraft.economy.zocker.pro.command.PayCommand;
import minecraft.economy.zocker.pro.command.EconomyCommand;
import minecraft.economy.zocker.pro.listener.ZockerDataInitializeListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import minecraft.economy.zocker.pro.api.VaultEconomy;

public class Main extends CorePlugin {

	public static Config ECONOMY_CONFIG;
	public static Config ECONOMY_MESSAGE;
	public static String ECONOMY_DATABASE_TABLE;

	private static CorePlugin PLUGIN;

	@Override
	public void onEnable() {
		super.onEnable();
		super.setDisplayItem(Material.GOLD_NUGGET);
		super.setHelpCommand("economy");
		this.setPluginName("MZP-Economy");

		PLUGIN = this;

		this.buildConfig();
		this.verifyDatabase();
		this.registerCommand();
		this.registerListener();

		// Vault
		getServer().getServicesManager().register(Economy.class, new VaultEconomy(), this, ServicePriority.Normal);
	}

	@Override
	public void registerCommand() {
		getCommand("balance").setExecutor(new BalanceInfoCommand());
		getCommand("economy").setExecutor(new EconomyCommand());
		getCommand("pay").setExecutor(new PayCommand());
	}

	@Override
	public void buildConfig() {
		ECONOMY_CONFIG = new Config("economy.yml", "MZP-Economy");

		// Global or per server economy
		ECONOMY_CONFIG.set("economy.global", false, "0.0.1");
		ECONOMY_CONFIG.set("economy.balance.start", 1000, "0.0.1");

		ECONOMY_CONFIG.save();


		ECONOMY_MESSAGE = new Config("message.yml", "MZP-Economy");

		ECONOMY_MESSAGE.set("economy.prefix", "&6&l[Economy] &3", "0.0.1");

		ECONOMY_MESSAGE.set("economy.player.offline", "Player &6%player% &3is not online.", "0.0.1");

		ECONOMY_MESSAGE.set("economy.currency.singular", "Crumb", "0.0.1");
		ECONOMY_MESSAGE.set("economy.currency.majority", "Crumbs", "0.0.1");

		ECONOMY_MESSAGE.set("economy.command.balance.insufficient", "You have got insufficient &6%currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.received", "You received from &6%player% %balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.self", "You cant send you self &6%currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.send", "You sent &6%player% %balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.info", "Your pocket contains &6%balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.add", "Player &6%player% &3added &6%balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.set", "Player &6%player% &3set &6%balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.remove", "Player &6%player% &3removed &6%balance% %currency%&3.", "0.0.1");
		ECONOMY_MESSAGE.set("economy.command.balance.check", "&6%player% &3 contains &6%balance% %currency%&3.", "0.0.1");


		ECONOMY_MESSAGE.save();
	}

	@Override
	public void registerListener() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new ZockerDataInitializeListener(), this);
	}

	@Override
	public void reload() {
		ECONOMY_CONFIG.reload();
		ECONOMY_MESSAGE.reload();
	}

	private void verifyDatabase() {
		if (ECONOMY_CONFIG.getBool("economy.global")) {
			ECONOMY_DATABASE_TABLE = "player_economy";
		} else {
			ECONOMY_DATABASE_TABLE = "player_economy_" + StorageManager.getServerName();
		}

		String createTable;
		if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_8)) {
			createTable = "CREATE TABLE IF NOT EXISTS '" + ECONOMY_DATABASE_TABLE + "' (uuid VARCHAR(36) NOT NULL UNIQUE, pocket DOUBLE NOT NULL, FOREIGN KEY (uuid) REFERENCES player (uuid) ON DELETE CASCADE);";
		} else {
			createTable = "CREATE TABLE IF NOT EXISTS " + ECONOMY_DATABASE_TABLE + " (uuid VARCHAR(36) NOT NULL UNIQUE, pocket DOUBLE NOT NULL, FOREIGN KEY (uuid) REFERENCES player (uuid) ON DELETE CASCADE);";
		}

		if (StorageManager.isMySQL()) {
			assert StorageManager.getMySQLDatabase() != null : "Create table failed.";
			StorageManager.getMySQLDatabase().createTable(createTable);
			return;
		}

		assert StorageManager.getSQLiteDatabase() != null : "Create table failed.";
		StorageManager.getSQLiteDatabase().createTable(createTable);
	}

	public static CorePlugin getPlugin() {
		return PLUGIN;
	}
}
