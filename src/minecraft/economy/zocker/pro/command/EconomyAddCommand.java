package minecraft.economy.zocker.pro.command;

import minecraft.core.zocker.pro.OfflineZocker;
import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.util.Util;
import minecraft.economy.zocker.pro.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.economy.zocker.pro.EconomyZocker;

import java.util.List;
import java.util.UUID;

public class EconomyAddCommand extends SubCommand {

	public EconomyAddCommand() {
		super("add", 2, 2);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage(getUsage());
			return;
		}

		Config message = Main.ECONOMY_MESSAGE;
		String prefix = message.getString("economy.prefix");
		Player target = Bukkit.getPlayer(args[0]);
		double amount = Double.valueOf(args[1]);

		if (target != null && target.isOnline()) {
			EconomyZocker economyZocker = new EconomyZocker(target.getUniqueId());
			economyZocker.setPocket(economyZocker.getPocket() + amount);

			if (amount <= 1) {
				CompatibleMessage.sendMessage(sender,
					prefix + message.getString("economy.command.balance.add")
						.replace("%player%", target.getName())
						.replace("%balance%", Util.formatInt((int) amount))
						.replace("%currency%", message.getString("economy.currency.singular")));
				return;
			}

			CompatibleMessage.sendMessage(sender,
				prefix + message.getString("economy.command.balance.add")
					.replace("%player%", target.getName())
					.replace("%balance%", Util.formatInt((int) amount))
					.replace("%currency%", message.getString("economy.currency.majority")));
			return;
		}

		UUID uuidOffline = OfflineZocker.fetchUUID(args[0]);
		if (uuidOffline == null) {
			sender.sendMessage(prefix + message.getString("economy.player.offline")
				.replace("%player%", args[0]));
			return;
		}

		EconomyZocker economyZocker = new EconomyZocker(uuidOffline);
		economyZocker.setPocket(economyZocker.getPocket() + amount);

		if (amount <= 1) {
			sender.sendMessage(prefix + message.getString("economy.command.balance.add")
				.replace("%player%", OfflineZocker.getName(uuidOffline))
				.replace("%balance%", Util.formatInt((int) amount))
				.replace("%currency%", message.getString("economy.currency.singular")));
			return;
		}

		sender.sendMessage(prefix + message.getString("economy.command.balance.add")
			.replace("%player%", OfflineZocker.getName(uuidOffline))
			.replace("%balance%", Util.formatInt((int) amount))
			.replace("%currency%", message.getString("economy.currency.majority")));
	}

	@Override
	public String getUsage() {
		return Main.ECONOMY_MESSAGE.getString("economy.prefix") + "/economy add <player> <" + Main.ECONOMY_MESSAGE.getString("economy.currency.majority") + ">";
	}


	@Override
	public String getPermission() {
		return "mzp.economy.balance.add";
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] strings) {
		return null;
	}
}
