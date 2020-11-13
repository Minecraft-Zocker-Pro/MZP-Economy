package minecraft.economy.zocker.pro;

import minecraft.core.zocker.pro.Zocker;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EconomyZocker extends Zocker {

	public EconomyZocker(Player player) {
		super(player);
	}

	public EconomyZocker(UUID uuid) {
		super(uuid);
	}

	public double getPocket() {
		CompletableFuture<String> pocketCompletableFuture = this.get(Main.ECONOMY_DATABASE_TABLE, "pocket");
		try {
			String pocket = pocketCompletableFuture.get();
			if (pocket == null) return -1;

			return Double.valueOf(pocket);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public CompletableFuture<Double> getPocketAsync() {
		return CompletableFuture.supplyAsync(this::getPocket);
	}

	public void setPocket(double pocket) {
		DecimalFormat df2 = new DecimalFormat(".##");
		String pocketFormatted = df2.format(pocket);
		pocketFormatted = pocketFormatted.replace(",", ".");

		this.set(Main.ECONOMY_DATABASE_TABLE, "pocket", pocketFormatted);
	}

	public boolean hasPocket() {
		CompletableFuture<String> pocketCompletableFuture = this.get(Main.ECONOMY_DATABASE_TABLE, "pocket");
		try {
			String pocket = pocketCompletableFuture.get();
			return pocket != null;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return false;
	}

	public CompletableFuture<Boolean> hasPocketAsync() {
		return CompletableFuture.supplyAsync(this::hasPocket);
	}
}