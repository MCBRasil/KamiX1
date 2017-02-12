package me.guigarciazinho.guimanagers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.guigarciazinho.models.PlayerGame;
import me.guigarciazinho.principal.Main;

public class DadosManager {

	private PlayerGame playerGame;
	private Main plugin;
	
	public DadosManager(Main main){
		plugin = main;
	}
	
	public void menuDados(OfflinePlayer alvo, Player p){
		playerGame = plugin.game.getPlayerGame(alvo.getUniqueId());
		Inventory inv = Bukkit.createInventory(null, 9, "§c§lDados do jogador.");
//		                   CABEÇA
		ItemStack cabeca = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta cabecaMeta = (SkullMeta) cabeca.getItemMeta();
		cabecaMeta.setOwner(alvo.getName());
		cabecaMeta.setDisplayName("§c" + alvo.getName());
		ArrayList<String> loreCabeca = new ArrayList<>();
		loreCabeca.add("§aEste é o perfil de §e§l" + alvo.getName() + "§a.");
		cabecaMeta.setLore(loreCabeca);
		cabeca.setItemMeta(cabecaMeta);
//		                  VITORIAS
		ItemStack vitorias = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta vitoriasMeta = vitorias.getItemMeta();
		vitoriasMeta.setDisplayName("§2Vitórias");
		ArrayList<String> loreVitorias = new ArrayList<>();
		loreVitorias.add("Partidas ganhas: §c"+ playerGame.getVitorias());
		vitoriasMeta.setLore(loreVitorias);
		vitorias.setItemMeta(vitoriasMeta);
//						DERROTAS
		ItemStack derrotas = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
		ItemMeta derrotasMeta = derrotas.getItemMeta();
		derrotasMeta.setDisplayName("§cDerrotas");
		ArrayList<String> loreDerrotas = new ArrayList<>();
		loreDerrotas.add("Partidas perdidas: §c"+ playerGame.getDerrotas());
		derrotasMeta.setLore(loreDerrotas);
		derrotas.setItemMeta(derrotasMeta);
//		               ONLINE
		if(alvo.isOnline()){
			ItemStack status = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta statusMeta = status.getItemMeta();
			statusMeta.setDisplayName("§2Online");
			status.setItemMeta(statusMeta);
			inv.setItem(8, status);
		}else{
			ItemStack status = new ItemStack(Material.REDSTONE_BLOCK);
			ItemMeta statusMeta = status.getItemMeta();
			statusMeta.setDisplayName("§cOffline");
			status.setItemMeta(statusMeta);
			inv.setItem(8, status);
		}
		inv.setItem(0, cabeca);
		inv.setItem(2, vitorias);
		inv.setItem(4, derrotas);
		p.openInventory(inv);
	}
}
