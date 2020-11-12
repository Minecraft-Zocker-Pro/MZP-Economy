package minecraft.economy.zocker.pro.api;

import minecraft.core.zocker.pro.util.Util;
import minecraft.economy.zocker.pro.EconomyZocker;
import minecraft.economy.zocker.pro.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class VaultEconomy implements Economy {

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "MZP-Economy";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String format(double v) {
		return Util.formatInt((int) v);
	}

	@Override
	public String currencyNamePlural() {
		return Main.ECONOMY_MESSAGE.getString("economy.currency.majority");
	}

	@Override
	public String currencyNameSingular() {
		return Main.ECONOMY_MESSAGE.getString("economy.currency.singular");
	}

	@Override
	public boolean hasAccount(String s) {
		return hasPocket(Bukkit.getPlayerExact(s));
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return hasPocketOffline(offlinePlayer.getUniqueId());
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return hasPocket(Bukkit.getPlayerExact(playerName));
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
		return hasPocketOffline(offlinePlayer.getUniqueId());
	}

	@Override
	public double getBalance(String playerName) {
		return getPocket(Bukkit.getPlayer(playerName));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return getPocketOffline(offlinePlayer.getUniqueId());
	}

	@Override
	public double getBalance(String playerName, String world) {
		return getPocket(Bukkit.getPlayer(playerName));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String world) {
		return getPocketOffline(offlinePlayer.getUniqueId());
	}

	@Override
	public boolean has(String playerName, double amount) {
		return hasEnough(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double amount) {
		return hasEnoughOffline(offlinePlayer.getUniqueId(), amount);
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return hasEnough(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
		return hasEnoughOffline(offlinePlayer.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		return withdrawPocket(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
		return withdrawPocketOffline(offlinePlayer.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		return withdrawPocket(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
		return withdrawPocketOffline(offlinePlayer.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		return depositPocket(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
		return depositPocketOffline(offlinePlayer.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		return depositPocket(Bukkit.getPlayer(playerName), amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
		return depositPocketOffline(offlinePlayer.getUniqueId(), amount);
	}

	/// region Bank

	@Override
	public EconomyResponse createBank(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	/// endregion Bank

	@Override
	public boolean createPlayerAccount(String s) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(String s, String s1) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		return false;
	}

	private double getPocket(Player player) {
		return new EconomyZocker(player.getUniqueId()).getPocket();
	}

	private double getPocketOffline(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			EconomyZocker economyZocker = new EconomyZocker(uuid);
			double pocket = economyZocker.getPocket();

			if (pocket == -1) return -1;

			return pocket;
		}

		if (player.isOnline()) {
			return getPocket(player);
		}

		return -1;
	}

	private boolean hasPocket(Player player) {
		return new EconomyZocker(player.getUniqueId()).hasPocket();
	}

	private boolean hasPocketOffline(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			EconomyZocker economyZocker = new EconomyZocker(uuid);

			double pocket = economyZocker.getPocket();
			return pocket != -1;
		}

		if (player.isOnline()) {
			return hasPocket(player);
		}

		return false;
	}

	private EconomyResponse depositPocket(Player player, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't deposit negative amount");
		}

		if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Zocker is null!");
		EconomyZocker economyZocker = new EconomyZocker(player.getUniqueId());

		double pocket = economyZocker.getPocket();
		if (pocket == -1) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Pocket is -1");
		pocket += amount;

		economyZocker.setPocket(pocket);

		return new EconomyResponse(amount, pocket, EconomyResponse.ResponseType.SUCCESS, null);
	}

	private EconomyResponse depositPocketOffline(UUID uuid, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't deposit negative amount");
		}

		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			EconomyZocker economyZocker = new EconomyZocker(uuid);

			double pocket = economyZocker.getPocket();
			if (pocket == -1) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Pocket is -1");

			pocket += amount;
			economyZocker.set(Main.ECONOMY_DATABASE_TABLE, "pocket", pocket);
			return new EconomyResponse(amount, pocket, EconomyResponse.ResponseType.SUCCESS, null);
		}

		if (player.isOnline()) {
			return depositPocket(player, amount);
		}

		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
	}

	private EconomyResponse withdrawPocket(Player player, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't deposit negative amount");
		}

		if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Zocker is null!");
		EconomyZocker economyZocker = new EconomyZocker(player.getUniqueId());

		double pocketAmount = economyZocker.getPocket();
		if (pocketAmount == -1) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Pocket is -1");
		}

		if (pocketAmount >= amount) {
			pocketAmount -= amount;

			economyZocker.setPocket(pocketAmount);

			return new EconomyResponse(amount, pocketAmount, EconomyResponse.ResponseType.SUCCESS, null);
		} else {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Not enough in your pocket.");
		}
	}

	private EconomyResponse withdrawPocketOffline(UUID uuid, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't deposit negative amount");
		}

		Player player = Bukkit.getPlayer(uuid);

		if (player == null) {
			EconomyZocker economyZocker = new EconomyZocker(uuid);

			double pocket = economyZocker.getPocket();
			if (pocket == -1) {
				return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Pocket is -1");
			}

			if (pocket >= amount) {
				pocket -= amount;

				economyZocker.set(Main.ECONOMY_DATABASE_TABLE, "pocket", pocket);

				return new EconomyResponse(amount, pocket, EconomyResponse.ResponseType.SUCCESS, null);
			} else {
				return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Not enough in your pocket.");
			}
		}

		if (player.isOnline()) {
			return withdrawPocket(player, amount);
		}

		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
	}

	private boolean hasEnough(Player player, double amount) {
		return new EconomyZocker(player.getUniqueId()).getPocket() >= amount;
	}

	private boolean hasEnoughOffline(UUID uuid, double amount) {
		Player player = Bukkit.getPlayer(uuid);

		if (player == null) {
			EconomyZocker economyZocker = new EconomyZocker(uuid);
			double pocket = economyZocker.getPocket();
			if (pocket == -1) return false;

			return pocket >= amount;
		}

		return hasEnough(player, amount);
	}
}
