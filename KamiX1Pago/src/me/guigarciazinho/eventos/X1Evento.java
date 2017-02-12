package me.guigarciazinho.eventos;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.guigarciazinho.models.Arena;
import me.guigarciazinho.models.PlayerGame;
import me.guigarciazinho.principal.Main;
import me.guigarciazinho.status.GameStatus;
import me.guigarciazinho.status.PlayerStatus;

public class X1Evento implements Listener{
	private Main plugin;
	private PlayerGame player;
	private PlayerGame playerAlvo;
	private Arena arena;
	
	public X1Evento(Main main){
	plugin = main;	
	}
	
	@EventHandler
	private void jogadorMorre(PlayerDeathEvent e){
		Player p = (Player) e.getEntity();
		Player killer = (Player) e.getEntity().getKiller();
		if(plugin.game.getDesafiado().contains(p.getUniqueId()) || plugin.game.getDesafiador().contains(killer.getUniqueId()) || plugin.game.getDesafiado().contains(killer.getUniqueId()) | plugin.game.getDesafiador().contains(p.getUniqueId())) {
			player = plugin.game.getPlayerGame(p.getUniqueId());
			arena = plugin.game.getArena(player.getArena());
		if(arena.getDesafiado().contains(p.getUniqueId())){
			arena.aoVencer(killer, p);
		}
		if(arena.getDesafiador().contains(p.getUniqueId())){
			arena.aoVencer(killer, p);
		}
		}else{
			
		}
	}
	
	@EventHandler
	private void jogadorSai(PlayerQuitEvent e){
		Player p = (Player) e.getPlayer();
		if(plugin.game.getDesafiado().contains(p.getUniqueId()) || plugin.game.getDesafiador().contains(p.getUniqueId())){
			player = plugin.game.getPlayerGame(p.getUniqueId());
			arena = plugin.game.getArena(player.getArena());
			if(arena.getDesafiado().contains(p.getUniqueId())){
				Player p2 = Bukkit.getPlayer(arena.getDesafiador().get(0));
				arena.aoVencer(p2, p);
			}
			if(arena.getDesafiador().contains(p.getUniqueId())){
				Player p2 = Bukkit.getPlayer(arena.getDesafiado().get(0));
				arena.aoVencer(p, p2);
			}
		}
	}
	
	@EventHandler
	private void jogadorTentarPegarDoChao(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		plugin.game.registrarPlayerGame(p.getUniqueId());
		player = plugin.game.getPlayerGame(p.getUniqueId());
		if(player.getStatus() == PlayerStatus.CAMAROTE){
			e.setCancelled(true);
		}else{
			
		}
	}
	
	@EventHandler
	private void aoBaterJogador(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			if(e.getEntity() instanceof Player){
			Player p = (Player) e.getDamager();
			Player alvo = (Player) e.getEntity();
			plugin.game.registrarPlayerGame(p.getUniqueId());
			player = plugin.game.getPlayerGame(p.getUniqueId());
			if(player.getStatus() == PlayerStatus.CAMAROTE){
				e.setCancelled(true);
			}
			plugin.game.registrarPlayerGame(alvo.getUniqueId());
			playerAlvo = plugin.game.getPlayerGame(alvo.getUniqueId());
			if(player.getStatus() == PlayerStatus.ONLINE && playerAlvo.getStatus() == PlayerStatus.IN_GAME){
				e.setCancelled(true);
			}
		}
		}
	}
	
	@EventHandler
	private void clicarInventario(InventoryClickEvent e){
		if(e.getInventory().getTitle().equalsIgnoreCase("§c§lDados do jogador.") || e.getInventory().getTitle().equalsIgnoreCase("§b§lInformações Arena") || e.getInventory().getTitle().equalsIgnoreCase("§c§lLista de Arenas")){
			e.setCancelled(true);
			if(e.getInventory().getTitle().equalsIgnoreCase("§c§lLista de Arenas")){
				if(e.getCurrentItem() != null){
					if(e.getCurrentItem().getItemMeta().getLore().contains("§cUm jogo está em andamento nesta arena")){
						String nome = e.getCurrentItem().getItemMeta().getDisplayName().replace("§cArena: §5", "");
						if(plugin.game.getArena(nome).getStatus() == GameStatus.IN_GAME){
						plugin.arenamanager.informacoesArena((Player) e.getWhoClicked(), nome);
						}else{
							e.getWhoClicked().closeInventory();
							Player p = (Player) e.getWhoClicked();
							p.performCommand("x1 arenas");
						}
					}
				}
			}
			
		}
	}
	
//	@EventHandler
//	private void aoEuEntrar(PlayerJoinEvent e){
//		if(e.getPlayer().getName().equalsIgnoreCase("guigarciazinho")){
//			e.getPlayer().sendMessage("§c============================");
//			e.getPlayer().sendMessage("[§eKamiX1§f]§2 Servidor utilizando §eKamiX1");
//			e.getPlayer().sendMessage("§c============================");
//			for(Player p : Bukkit.getOnlinePlayers()){
//				if(p.hasPermission("x1.admin")){
//					p.sendMessage("§c----------- ATENÇÃO ----------");
//					p.sendMessage("§2 O DESENVOLVEDOR DO PLUGIN §eKamiX1§e,§a guigarciazinho§e, ENTROU NO SERVIDOR");
//					p.sendMessage("§c----------- ATENÇÃO ----------");
//					p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
//					p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1, 1);
//				}
//			}
//			
//		}
//		
//	}

}
