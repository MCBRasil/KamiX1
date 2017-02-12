package me.guigarciazinho.principal;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.guigarciazinho.comandos.Local;
import me.guigarciazinho.comandos.X1;
import me.guigarciazinho.eventos.X1Evento;
import me.guigarciazinho.guimanagers.ArenaManager;
import me.guigarciazinho.guimanagers.DadosManager;
import me.guigarciazinho.models.Game;
import me.guigarciazinho.mysql.BancoDeDados;
import me.guigarciazinho.util.GhostMaker;

public class Main extends JavaPlugin {
	public X1 x1;
	public Game game;
	public X1Evento x1evento;
	public BancoDeDados bancodedados;
	public DadosManager dadosmanager;
	public GhostMaker ghostmaker;
	public ArenaManager arenamanager;
	public static File configFile;
	public static YamlConfiguration configLoc;
	
	public void onEnable(){
		System.out.println("[KamiX1] Habilitando...");
		saveDefaultConfig();
		arenamanager = new ArenaManager(this);
		bancodedados = new BancoDeDados(this);
		x1evento = new X1Evento(this);
		ghostmaker = new GhostMaker(this);
		game = new Game(this);
		x1 = new X1(this);
		dadosmanager = new DadosManager(this);
		getCommand("x1").setExecutor(new X1(this));
		getCommand("setx1").setExecutor(new Local(this));
		getServer().getPluginManager().registerEvents(x1evento, this);
		configFile = new File(getDataFolder(), "Locations.yml");
		if (!configFile.exists()) {
			saveResource("Locations.yml", false);
		}
		configLoc = YamlConfiguration.loadConfiguration(configFile);
		try {
			configLoc.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[KamiX1] Plugin habilitado com sucesso!");
	}
	
	public void onDisable(){
		System.out.println("[KamiX1] Desabilitando...");
		
		System.out.println("[KamiX1] Plugin desabilitado com sucesso!");
		
	}
	

}
