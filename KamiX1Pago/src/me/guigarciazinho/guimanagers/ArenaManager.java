package me.guigarciazinho.guimanagers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.guigarciazinho.models.Arena;
import me.guigarciazinho.principal.Main;
import me.guigarciazinho.status.GameStatus;

public class ArenaManager {
	private Main plugin;
	private Arena arena;

	public ArenaManager(Main main) {
		plugin = main;
	}

	@SuppressWarnings("static-access")
	public void mostrarArenas(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9 * 5, "§c§lLista de Arenas");
		for (String s : plugin.configLoc.getConfigurationSection("Arenas.").getKeys(false)) {
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§cArena: §5" + plugin.configLoc.getString("Arenas." + s + ".Nome"));
			ArrayList<String> loreItem = new ArrayList<>();
			plugin.game.registrarArena(s);
			arena = plugin.game.getArena(s);
			if (arena.getStatus() == GameStatus.IN_GAME) {
				loreItem.add("§cUm jogo está em andamento nesta arena");
				loreItem.add("§eClique para exibir mais informações");
				itemMeta.setLore(loreItem);
			} else if (arena.getStatus() == GameStatus.PENDING) {
				loreItem.add("§eEsta arena tem uma solicitacao pendente");
				itemMeta.setLore(loreItem);
			} else {
				loreItem.add("§2A arena está disponível");
				itemMeta.setLore(loreItem);
			}
			item.setItemMeta(itemMeta);
			inv.addItem(item);

		}
		p.openInventory(inv);
	}
	
	public void informacoesArena(Player p, String nome){
		Inventory inv = Bukkit.createInventory(null, 9, "§b§lInformações Arena");
		arena = plugin.game.getArena(nome);
		
		
		Player desafiador = Bukkit.getPlayer(arena.getDesafiador().get(0)).getPlayer();
		Player desafiado = Bukkit.getPlayer(arena.getDesafiado().get(0)).getPlayer();
		ItemStack cabecaDesafiador = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta cabecaMeta = (SkullMeta) cabecaDesafiador.getItemMeta();
		cabecaMeta.setOwner(desafiador.getName());
		cabecaMeta.setDisplayName(desafiador.getName());
		ArrayList<String> loreCabecaDesafiador = new ArrayList<>();
		loreCabecaDesafiador.add("§5§lConvidante");
		cabecaMeta.setLore(loreCabecaDesafiador);
		cabecaDesafiador.setItemMeta(cabecaMeta);
		
		ItemStack cabecaDesafiado = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta cabecaMetaD = (SkullMeta) cabecaDesafiado.getItemMeta();
		cabecaMetaD.setOwner(desafiado.getName());
		cabecaMetaD.setDisplayName(desafiado.getName());
		ArrayList<String> loreCabecaDesafiado = new ArrayList<>();
		loreCabecaDesafiado.add("§5§lConvidado");
		cabecaMetaD.setLore(loreCabecaDesafiado);
		cabecaDesafiado.setItemMeta(cabecaMetaD);
		
		ItemStack versus = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta versusMeta = versus.getItemMeta();
		versusMeta.setDisplayName("§4§lVERSUS");
		versus.setItemMeta(versusMeta);
		
		
		inv.addItem(cabecaDesafiador);
		inv.setItem(4, versus);
		inv.setItem(8, cabecaDesafiado);
		p.openInventory(inv);
	}
	
}
