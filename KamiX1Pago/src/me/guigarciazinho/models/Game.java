package me.guigarciazinho.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.guigarciazinho.principal.Main;
import me.guigarciazinho.status.GameStatus;
import net.milkbowl.vault.economy.Economy;

public class Game {
	private Main plugin = null;
	private GameStatus gameStatus = null;
	private Economy econ = null;
	@SuppressWarnings("unused")
	private PlayerGame player;
	private ArrayList<UUID> desafiador;
	private ArrayList<UUID> desafiado;
	private HashMap<UUID, PlayerGame> playersInGame;
	private HashMap<String, Arena> arena;
	
	public Game(Main main){
		plugin = main;	
		desafiador = new ArrayList<>();
		desafiado = new ArrayList<>();
		playersInGame = new HashMap<>();
		arena = new HashMap<>();
		setStatus(GameStatus.OPEN);
		setupEconomy();
		
	}
	
	public ArrayList<UUID> getDesafiador(){
		return desafiador;
	}
	
	public ArrayList<UUID> getDesafiado(){
		return desafiado;
	}
	
	public void setStatus(GameStatus status){
		this.gameStatus = status;
	}
	
	public GameStatus getStatus(){
		return gameStatus;
	}
	
	@SuppressWarnings("static-access")
	public boolean registrarArena(String nome){
		if(!arena.containsKey(nome)){
			Location loc1 = (Location) plugin.configLoc.get("Arenas." + nome + "." + "Loc1." + ".Location");
			Location loc2 = (Location) plugin.configLoc.get("Arenas." + nome + "." + "Loc2." + ".Location");
			Location saida = (Location) plugin.configLoc.get("Arenas." + nome + "." + "Saida." + ".Location");
			Location camarote = (Location) plugin.configLoc.get("Arenas." + nome + "." + "Camarote." + ".Location");
			arena.put(nome, new Arena(nome, loc1, loc2, saida, camarote, plugin, econ));
			return true;
		}
		return false;
	}
	
	public Economy getEcon(){
		return econ;
	}
	
	public Arena getArena(String nome){
		if(arena.containsKey(nome)){
			return arena.get(nome);
		}
		return null;
	}
	
	public boolean registrarPlayerGame(UUID id){
		if(!playersInGame.containsKey(id)){
			playersInGame.put(id,  player = new PlayerGame(id));
			return true;
		}
		return false;
	}
	
	public PlayerGame getPlayerGame(UUID id){
		if(playersInGame.containsKey(id)){
			return playersInGame.get(id);
		}
		return null;
	}

	
	
	@SuppressWarnings("deprecation")
	public double getDinheiro(String nome){
		
		return econ.getBalance(nome);
	}
	
	public String getConfigString(String string){
		return plugin.getConfig().getString(string).replace("&", "§");
	}
	
	
	public double getAposta(){
		return plugin.getConfig().getDouble("Dinheiro");
	}
	
	public int getTempo(String string){
		return plugin.getConfig().getInt(string);
		
	}

	
	
	
	private boolean setupEconomy() {
		if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public void registrarJogadores(UUID idDesafiador, String arena, UUID idDesafiado) {
		desafiador.add(idDesafiador);
		desafiado.add(idDesafiado);
	}


}
