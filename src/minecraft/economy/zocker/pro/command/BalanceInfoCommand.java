package minecraft.economy.zocker.pro.command;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.command.Command;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.util.Util;
import minecraft.economy.zocker.pro.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import minecraft.core.zocker.pro.config.Config;
import minecraft.economy.zocker.pro.EconomyZocker;

import java.util.List;

public class BalanceInfoCommand extends Command {

	public BalanceInfoCommand() {
		super("balance", "mzp.economy.balance.info", new String[]{"pocket", "mone", "money", "geld", "crumbs", "crumb", "cash"});
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public void onExecute(CommandSender sender, String[] strings) {
		if (sender instanceof Player) {

			Player player = (Player) sender;
			Zocker zocker = Zocker.getZocker(player.getUniqueId());
			if (zocker == null) return;

			double pocket = new EconomyZocker(zocker.getUUID()).getPocket();
			Config message = Main.ECONOMY_MESSAGE;
			String prefix = message.getString("economy.prefix");

			int pocketInt = (int) pocket;
			if (pocketInt <= 1) {
				CompatibleMessage.sendMessage(player, prefix + message.getString("economy.command.balance.info")
					.replace("%balance%", Util.formatInt(pocketInt))
					.replace("%currency%", message.getString("economy.currency.singular")));
			} else {
				CompatibleMessage.sendMessage(player,
					prefix + message.getString("economy.command.balance.info")
						.replace("%balance%", Util.formatInt(pocketInt))
						.replace("%currency%", message.getString("economy.currency.majority")));
			}
		}
	}
}
