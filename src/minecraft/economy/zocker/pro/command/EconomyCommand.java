package minecraft.economy.zocker.pro.command;

import minecraft.core.zocker.pro.command.Command;
import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class EconomyCommand extends Command {

	private static final List<SubCommand> SUB_COMMANDS = new ArrayList<>();

	public EconomyCommand() {
		super("economy", "mzp.economy", new String[]{});

		SUB_COMMANDS.add(new EconomyCheckCommand());
		SUB_COMMANDS.add(new EconomySetCommand());
		SUB_COMMANDS.add(new EconomyAddCommand());
		SUB_COMMANDS.add(new EconomyRemoveCommand());
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			CompatibleMessage.sendMessage(sender, "§7--------------------");
			CompatibleMessage.sendMessage(sender, "§6§lCommands");
			CompatibleMessage.sendMessage(sender, "§3/economy check <zocker>");
			CompatibleMessage.sendMessage(sender, "§3/economy set <zocker> <crumbs>");
			CompatibleMessage.sendMessage(sender, "§3/economy add <zocker> <crumbs>");
			CompatibleMessage.sendMessage(sender, "§3/economy remove <zocker> <crumbs>");
			CompatibleMessage.sendMessage(sender, "§7--------------------");
			return;
		}

		for (SubCommand subCommand : SUB_COMMANDS) {
			if (subCommand.getName().equalsIgnoreCase(args[0])) {
				subCommand.execute(sender, args);
			}
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 0) {
			SUB_COMMANDS.forEach(subCommand -> completions.add(subCommand.getName()));
		} else if (args.length == 1) {
			SUB_COMMANDS.stream().filter(subCommand -> subCommand.getName().toLowerCase().startsWith(args[0].toLowerCase()))
				.forEach(subCommand -> completions.add(subCommand.getName()));
		} else {
			SubCommand command = findSubCommand(args[0]);

			if (command != null) {
				return command.getCompletions(sender, args);
			}
		}

		return completions;
	}

	private SubCommand findSubCommand(String name) {
		return SUB_COMMANDS.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}