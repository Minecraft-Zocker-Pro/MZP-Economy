package minecraft.economy.zocker.pro.command;

import minecraft.core.zocker.pro.OfflineZocker;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.util.Util;
import minecraft.economy.zocker.pro.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.economy.zocker.pro.EconomyZocker;

import java.util.List;
import java.util.UUID;

public class EconomyCheckCommand extends SubCommand {

	public EconomyCheckCommand() {
		super("check", 1, 1);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			CompatibleMessage.sendMessage(sender, getUsage());
			return;
		}

		Player target = Bukkit.getPlayer(args[0]);
		Config message = Main.ECONOMY_MESSAGE;
		String prefix = message.getString("economy.prefix");

		if (target != null && target.isOnline()) {
			EconomyZocker economyZocker = new EconomyZocker(target.getUniqueId());
			double pocket = economyZocker.getPocket();
			if (pocket <= 1) {
				CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.check")
					.replace("%player%", target.getName())
					.replace("%balance%", Util.formatInt((int) pocket))
					.replace("%currency%", message.getString("economy.currency.singular")));

				return;
			}

			CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.check")
				.replace("%player%", target.getName())
				.replace("%balance%", Util.formatInt((int) pocket))
				.replace("%currency%", message.getString("economy.currency.majority")));

			return;
		}

		UUID uuidOffline = OfflineZocker.fetchUUID(args[0]);
		if (uuidOffline == null) {
			CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.player.offline")
				.replace("%player%", args[0]));
			return;
		}

		double pocket = new EconomyZocker(uuidOffline).getPocket();

		if (pocket <= 1) {
			CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.check")
				.replace("%player%", OfflineZocker.getName(uuidOffline))
				.replace("%balance%", Util.formatInt((int) pocket))
				.replace("%currency%", message.getString("economy.currency.singular")));

			return;
		}

		CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.check")
			.replace("%player%", args[0])
			.replace("%balance%", Util.formatInt((int) pocket))
			.replace("%currency%", message.getString("economy.currency.majority")));
	}

	@Override
	public String getUsage() {
		return Main.ECONOMY_MESSAGE.getString("economy.prefix") + "/economy check <player>";
	}

	@Override
	public String getPermission() {
		return "mzp.economy.balance.check";
	}


	@Override
	public List<String> getCompletions(CommandSender sender, String[] strings) {
		return null;
	}
}
