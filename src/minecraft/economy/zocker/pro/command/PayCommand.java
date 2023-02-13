package minecraft.economy.zocker.pro.command;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.command.Command;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.util.Util;
import minecraft.economy.zocker.pro.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.economy.zocker.pro.EconomyZocker;

import java.util.List;

public class PayCommand extends Command {

	public PayCommand() {
		super("pay", "mzp.economy.pay", new String[]{""});
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
		return null;
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Zocker zocker = Zocker.getZocker(((Player) sender));
			if (zocker == null) return;

			EconomyZocker economyZocker = new EconomyZocker(zocker.getUUID());
			Config message = Main.ECONOMY_MESSAGE;
			String prefix = message.getString("economy.prefix");

			if (args.length <= 1) {
				CompatibleMessage.sendMessage(sender, prefix + "Type §6/pay <player> <amount>");
				return;
			}

			if (args.length == 2) {
				if (sender.getName().equals(args[0])) {
					CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.self")
						.replace("%currency%", message.getString("economy.currency.singular")));
					return;
				}

				Player playerReceiver = Bukkit.getPlayer(args[0]);

				if (playerReceiver != null) {
					if (playerReceiver.isOnline()) {
						if (Character.isDigit(args[1].toCharArray()[0])) {
							double balance = economyZocker.getPocket();
							double amount = Double.parseDouble(args[1]);

							// check if player send minimum amount
							if (amount < Main.ECONOMY_CONFIG.getDouble("economy.pay.minimum.amount")) {
								CompatibleMessage.sendMessage(zocker.getPlayer(), prefix + message.getString("economy.command.pay.minimum.amount")
									.replace("%currency%", message.getString("economy.currency.singular"))
									.replace("%amount%", Main.ECONOMY_CONFIG.getString("economy.pay.minimum.amount")));
								CompatibleSound.playErrorSound(zocker.getPlayer());
								return;
							}

							if (Main.ECONOMY_CONFIG.getBool("economy.pay.fee.enabled")) {
								double feeFixed = Main.ECONOMY_CONFIG.getDouble("economy.pay.fee.fixed");

								double feeAmount = 0;

								double feePercent = Main.ECONOMY_CONFIG.getDouble("economy.pay.fee.percent");
								if (feePercent > 0) {

									feeAmount = amount * feePercent / 100;
								}

								if (balance <= (amount + feeAmount + feeFixed)) {
									CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.pay.fee.insufficient")
										.replace("%currency%", message.getString("economy.currency.majority"))
										.replace("%fee%", Util.formatInt((int) (feeFixed + feeAmount))));
									CompatibleSound.playErrorSound(zocker.getPlayer());
									return;
								}

								economyZocker.setPocket(economyZocker.getPocket() - (feeAmount + feeFixed));
							}

							if (balance >= amount) {
								EconomyZocker zockerReceiverEconomy = new EconomyZocker(playerReceiver.getUniqueId());
								zockerReceiverEconomy.setPocket(zockerReceiverEconomy.getPocket() + amount);

								economyZocker.setPocket(economyZocker.getPocket() - amount);

								if (amount <= 1) {
									CompatibleMessage.sendMessage(playerReceiver, prefix + message.getString("economy.command.balance.received")
										.replace("%player%", sender.getName())
										.replace("%balance%", Util.formatInt((int) amount))
										.replace("%currency%", message.getString("economy.currency.singular")));

									CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.send")
										.replace("%player%", playerReceiver.getName())
										.replace("%balance%", Util.formatInt((int) amount))
										.replace("%currency%", message.getString("economy.currency.singular")));
									return;
								}

								CompatibleMessage.sendMessage(playerReceiver, prefix + message.getString("economy.command.balance.received")
									.replace("%player%", sender.getName())
									.replace("%balance%", Util.formatInt((int) amount))
									.replace("%currency%", message.getString("economy.currency.majority")));

								CompatibleSound.ENTITY_CHICKEN_EGG.play(playerReceiver);

								CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.send")
									.replace("%player%", playerReceiver.getName())
									.replace("%balance%", Util.formatInt((int) amount))
									.replace("%currency%", message.getString("economy.currency.majority")));
								return;
							}

							CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.command.balance.insufficient")
								.replace("%currency%", message.getString("economy.currency.majority")));
							CompatibleSound.playErrorSound(zocker.getPlayer());
							return;
						}

						CompatibleMessage.sendMessage(sender, prefix + "Type §6/pay <player> <amount>");
						return;
					}
				}
				CompatibleMessage.sendMessage(sender, prefix + message.getString("economy.player.offline").replace("%player%", args[0]));
			}
		}
	}
}
