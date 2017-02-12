package me.guigarciazinho.models;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.guigarciazinho.principal.Main;
import me.guigarciazinho.status.GameStatus;
import me.guigarciazinho.status.PlayerStatus;
import net.milkbowl.vault.economy.Economy;

public class Arena {
	private String nome;
	private GameStatus status;
	private int tempoRestante;
	private ArrayList<UUID> desafiador;
	private ArrayList<UUID> desafiado;
	private Location loc1;
	private Location loc2;
	private Location saida;
	@SuppressWarnings("unused")
	private Location camarote;
	private Economy econ;
	private Main plugin;
	private BukkitTask task;
	private BukkitTask task1;

	public Arena(String nome, Location loc1, Location loc2, Location saida, Location camarote, Main main,
			Economy econ) {
		desafiador = new ArrayList<>();
		desafiado = new ArrayList<>();
		this.nome = nome;
		this.econ = econ;
		this.plugin = main;
		this.tempoRestante = plugin.getConfig().getInt("Tempo");
		this.status = GameStatus.OPEN;
		this.loc1 = loc1;
		this.loc2 = loc2;
		this.saida = saida;
		this.camarote = camarote;

	}

	public void registrarJogadores(UUID idDesafiador, UUID idAlvo) {
		desafiador.add(idDesafiador);
		desafiado.add(idAlvo);

	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public GameStatus getStatus() {
		return this.status;
	}
	
	public int getTempo(){
		return this.tempoRestante;
	}

	public ArrayList<UUID> getDesafiado() {
		return desafiado;
	}

	public ArrayList<UUID> getDesafiador() {
		return desafiador;
	}

	public void desregistrarJogadores() {
		desafiado.clear();
		desafiador.clear();
	}

	public void jogoAceito(Player p) {
		Bukkit.getScheduler().cancelTasks(plugin);
		setStatus(GameStatus.IN_GAME);
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo")
				+ plugin.game.getConfigString("Aceitou").replace("@desafiado", p.getName()));
		Bukkit.getPlayer(desafiador.get(0)).teleport(this.loc1);
		Bukkit.getPlayer(desafiado.get(0)).teleport(this.loc2);
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + "§e Para assistir o x1 utilize /x1 camarote");
		task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				tempoAcabou();

			}
		}, 20 * plugin.game.getTempo("Tempo"));
	}

	public void tempoAcabou() {
		Bukkit.getScheduler().cancelTask(task.getTaskId());
		Bukkit.getPlayer(desafiado.get(0)).teleport(this.saida);
		Bukkit.getPlayer(desafiador.get(0)).teleport(this.saida);
		plugin.game.getPlayerGame(getDesafiador().get(0)).setStatus(PlayerStatus.ONLINE);
		plugin.game.getPlayerGame(getDesafiado().get(0)).setStatus(PlayerStatus.ONLINE);
		desregistrarJogadores();
		setStatus(GameStatus.OPEN);
		tempoRestante = plugin.getConfig().getInt("Tempo");
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Tempo acabou"));
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Arena disponivel").replace("@arena", this.nome));
	}

	@SuppressWarnings({ "deprecation" })
	public void aoVencer(Player vencedor, Player perdedor) {
		Bukkit.getScheduler().cancelTask(task.getTaskId());
		vencedor.sendMessage(plugin.game.getConfigString("Prefixo") + "§e Você tem §2"
				+ plugin.getConfig().getInt("Tempo de coleta") + "§e segundos para pegar os itens do chão.");
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Venceu")
				.replace("@vencedor", vencedor.getName()).replace("@perdedor", perdedor.getName()));
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				vencedor.teleport(saida);

			}
		}, 20 * plugin.getConfig().getInt("Tempo de coleta"));
		plugin.bancodedados.gerenciarVencedor(vencedor.getUniqueId(), vencedor.getName());
		plugin.bancodedados.gerenciarPerdedor(perdedor.getUniqueId(), perdedor.getName());
		econ.withdrawPlayer(perdedor.getName(), plugin.game.getAposta());
		econ.depositPlayer(vencedor.getName(), plugin.game.getAposta());
		desregistrarJogadores();
		tempoRestante = plugin.getConfig().getInt("Tempo");
		desregistrarJogadores();
		setStatus(GameStatus.OPEN);
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Arena disponivel").replace("@arena", this.nome));
		plugin.game.getPlayerGame(vencedor.getUniqueId()).setStatus(PlayerStatus.ONLINE);
		plugin.game.getPlayerGame(perdedor.getUniqueId()).setStatus(PlayerStatus.ONLINE);

	}

	public void tempoArregar(Player p, OfflinePlayer alvo) {
		task1 = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				aoArregar(p, alvo);

			}
		}, 20 * plugin.game.getTempo("Tempo pend"));
	}

	public void aoArregar(Player p, OfflinePlayer alvo) {
		desregistrarJogadores();
		plugin.game.getPlayerGame(p.getUniqueId()).setStatus(PlayerStatus.ONLINE);
		plugin.game.getPlayerGame(alvo.getUniqueId()).setStatus(PlayerStatus.ONLINE);
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Arregou")
				.replace("@desafiador", p.getName()).replace("@desafiado", alvo.getName()));
		Bukkit.getScheduler().cancelTask(task1.getTaskId());
		setStatus(GameStatus.OPEN);
		Bukkit.broadcastMessage(plugin.game.getConfigString("Prefixo") + plugin.game.getConfigString("Arena disponivel").replace("@arena", this.nome));
	}


}
