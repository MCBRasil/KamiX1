package me.guigarciazinho.comandos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.guigarciazinho.models.Arena;
import me.guigarciazinho.models.PlayerGame;
import me.guigarciazinho.principal.Main;
import me.guigarciazinho.status.GameStatus;
import me.guigarciazinho.status.PlayerStatus;

public class X1 implements CommandExecutor {
	private Main plugin;
	private String prefixo;
	private static PlayerGame player = null;
	private static PlayerGame playerAlvo = null;
	private Arena arena;
	BukkitTask task1;

	public X1(Main main) {
		this.plugin = main;
		prefixo = plugin.getConfig().getString("Prefixo").replace("&", "§");
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("x1")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length >= 1) {

					if ("camarote".equalsIgnoreCase(args[0])) {
						if (p.hasPermission("x1.camarote") || p.hasPermission("x1.admin")) {
							if (args.length >= 2) {
								if(!plugin.configLoc.contains("Arenas." + args[1])){
									p.sendMessage(prefixo + plugin.game.getConfigString("Null arena"));
									return true;
								}
								if (checarInv(p) == false && checarArmadura(p) == true) {
									plugin.game.registrarArena(args[1]);
									arena = plugin.game.getArena(args[1]);
									plugin.game.registrarPlayerGame(p.getUniqueId());
									player = plugin.game.getPlayerGame(p.getUniqueId());
									if (player.getStatus() == PlayerStatus.ONLINE) {
										player.setStatus(PlayerStatus.CAMAROTE);
										plugin.ghostmaker.setAsGhost(p, args[1]);
										return true;
									}
									player.setStatus(PlayerStatus.ONLINE);
									plugin.ghostmaker.setAsNormal(p, args[1]);
									return true;
								} else {
									p.sendMessage(prefixo + plugin.game.getConfigString("Esvazie o inventario"));
									return true;
								}
							} else {
								p.sendMessage(prefixo + plugin.game.getConfigString("Sem permissao para camarote"));
								return true;
							}
						}
					}

					if ("arenas".equalsIgnoreCase(args[0])) {
						plugin.arenamanager.mostrarArenas(p);
						return true;
					}

					if ("encerrar".equalsIgnoreCase(args[0])) {
						if (p.hasPermission("x1.admin")) {
							if (args.length >= 2) {
								if (!plugin.configLoc.contains(args[2])) {
									return true;
								}
								arena = plugin.game.getArena(args[2]);
								if (arena.getStatus() == GameStatus.IN_GAME) {
									arena.tempoAcabou();
									return true;
								} else {
									p.sendMessage("[§eKamiX1§f]§c Não há nenhum x1 em andamento para ser encerrado");
									return true;
								}
							} else {
								p.sendMessage("[§eKamiX1§f]§c Formato errado. Use /x1 encerrar NomeDaArena");
							}
						} else {
							p.sendMessage(prefixo + plugin.game.getConfigString("Sem permissao para encerrar"));
							return true;
						}
					}

					if ("dados".equalsIgnoreCase(args[0])) {
						OfflinePlayer alvo = Bukkit.getOfflinePlayer(args[1]);
						if (alvo.getName().equalsIgnoreCase(args[1])) {
							if (alvo.hasPlayedBefore()) {
								plugin.game.registrarPlayerGame(alvo.getUniqueId());
								player = plugin.game.getPlayerGame(alvo.getUniqueId());
								if (player.getCarregado() == false) {
									plugin.bancodedados.getDados(alvo.getUniqueId());
								}
								plugin.dadosmanager.menuDados(alvo, p);
								return true;
							}
						}

					}

					// if("apostar".equalsIgnoreCase(args[0])){
					// if(plugin.game.getStatus() == GameStatus.PENDING){
					//
					// }
					//
					// }
					// if("reload".equalsIgnoreCase(args[0])){
					// if(p.hasPermission("x1.admin")){
					// if(plugin.game.getStatus() == GameStatus.IN_GAME){
					// plugin.game.tempoAcabou();
					// }
					// plugin.reloadConfig();
					// plugin.getPluginLoader().disablePlugin(plugin);
					// plugin.getPluginLoader().enablePlugin(plugin);
					// p.sendMessage("[§eKamiX1§f]§2 Config recarregada com
					// sucesso!");
					// return true;
					// }
					// }

					if ("aceitar".equalsIgnoreCase(args[0])) {
						if (plugin.game.getDesafiado().contains(p.getUniqueId())) {
							player = plugin.game.getPlayerGame(p.getUniqueId());
							String arena = player.getArena();
							this.arena = plugin.game.getArena(arena);
							Player desafiador = Bukkit.getPlayer(this.arena.getDesafiador().get(0));
							playerAlvo = plugin.game.getPlayerGame(desafiador.getUniqueId());
							player.setStatus(PlayerStatus.IN_GAME);
							playerAlvo.setStatus(PlayerStatus.IN_GAME);
							this.arena.jogoAceito(p);
							Location loc = (Location) plugin.configLoc
									.get("Arenas." + arena + "." + "Loc1." + ".Location");
							Location loc2 = (Location) plugin.configLoc
									.get("Arenas." + arena + "." + "Loc2." + ".Location");
							p.teleport(loc2);
							desafiador.teleport(loc);
							return true;
						} else {
							p.sendMessage(prefixo + plugin.game.getConfigString("Sem solicitacao"));
							return true;
						}
					}

					if ("desafiar".equalsIgnoreCase(args[0])) {
						if (args.length >= 3) {
							OfflinePlayer alvo = Bukkit.getOfflinePlayer(args[1]);
							if (p.getName().equalsIgnoreCase(args[1])) {
								p.sendMessage(prefixo + plugin.game.getConfigString("Si mesmo"));
								return true;
							}
							if (alvo.getName().equalsIgnoreCase(args[1])) {
								if (alvo.isOnline()) {
									if (plugin.game.getDinheiro(alvo.getName()) >= plugin.game.getAposta()) {
										if (plugin.game.getDinheiro(p.getName()) >= plugin.game.getAposta()) {
											if (!plugin.configLoc.contains("Arenas." + args[2])) {
												p.sendMessage(prefixo + plugin.game.getConfigString("Null arena"));
												return true;
											}
											plugin.game.registrarPlayerGame(p.getUniqueId());
											player = plugin.game.getPlayerGame(p.getUniqueId());
											plugin.game.registrarPlayerGame(alvo.getUniqueId());
											playerAlvo = plugin.game.getPlayerGame(alvo.getUniqueId());
											plugin.game.registrarArena(args[2]);
											arena = plugin.game.getArena(args[2]);
											if (playerAlvo.getStatus() != PlayerStatus.ONLINE) {
												p.sendMessage(
														prefixo + plugin.game.getConfigString("Convidar ocupado"));
												return true;
											}
											if (arena.getStatus() != GameStatus.OPEN) {
												p.sendMessage(prefixo + plugin.game.getConfigString("Arena em uso"));
												return true;
											}
											if (player.getStatus() != PlayerStatus.ONLINE) {
												p.sendMessage(prefixo
														+ plugin.game.getConfigString("Impossibilitado de desafiar"));
												return true;
											}
											plugin.game.registrarJogadores(p.getUniqueId(), args[2],
													alvo.getUniqueId());
											plugin.game.registrarArena(args[2]);
											player.setArena(args[2]);
											playerAlvo.setArena(args[2]);
											player.setStatus(PlayerStatus.PENDING);
											playerAlvo.setStatus(PlayerStatus.PENDING);
											arena.setStatus(GameStatus.PENDING);
											Bukkit.broadcastMessage(prefixo + plugin.getConfig().getString("Desafiou")
													.replace("@desafiador", p.getName())
													.replace("@desafiado", alvo.getName()).replace("&", "§"));
											Bukkit.getPlayer(args[1])
													.sendMessage(prefixo + "§2 Para aceitar o x1 utilize /x1 aceitar");
											arena.registrarJogadores(p.getUniqueId(), alvo.getUniqueId());
											arena.tempoArregar(p, alvo);

											return true;
										} else {
											p.sendMessage(prefixo + " §c Você não tem dinheiro para tirar um x1");
											return true;
										}
									} else {
										p.sendMessage(prefixo + plugin.game.getConfigString("Alvo sem dinheiro")
												.replace("@player", alvo.getName()));
										return true;
									}
								} else {
									p.sendMessage(prefixo + plugin.game.getConfigString("Jogador offline")
											.replace("@player", alvo.getName()));
								}
							}

						} else {
							sender.sendMessage(prefixo + plugin.game.getConfigString("Uso incorreto"));
							return true;
						}
					}
				} else {
					sender.sendMessage(prefixo + plugin.game.getConfigString("Uso simples"));
					return true;
				}
			} else {
				sender.sendMessage(prefixo + "§cApena para players.");
				return true;
			}
		}
		return false;
	}

	public boolean checarInv(Player p) {
		for (ItemStack item : p.getInventory().getContents()) {
			if (item != null) {
				return true;
			}

		}
		return false;

	}

	public boolean checarArmadura(Player p) {
		if (p.getInventory().getHelmet() == null && p.getInventory().getChestplate() == null
				&& p.getInventory().getLeggings() == null && p.getInventory().getBoots() == null) {
			return true;
		}
		return false;
	}

}
