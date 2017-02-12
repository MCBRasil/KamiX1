package me.guigarciazinho.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.guigarciazinho.principal.Main;

public class GhostMaker {
	private Main plugin;
	private Team team;

	public GhostMaker(Main main) {
		plugin = main;
		createGetTeam();
	}
	private void createGetTeam() {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        team = board.getTeam("camarote");
        if (team == null) {
        	team = board.registerNewTeam("camarote");
        }
        team.setCanSeeFriendlyInvisibles(true);
    }

	@SuppressWarnings("static-access")
	public void setAsGhost(Player p, String arena) {
			team.addPlayer(p);
			PotionEffect potion = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15);
			p.addPotionEffect(potion);
			Location loc = (Location) plugin.configLoc.get("Arenas." + arena+"." + "Camarote." + ".Location");
			p.setGameMode(GameMode.ADVENTURE);
			p.setAllowFlight(true);
			p.teleport(loc);
	}
	
	@SuppressWarnings("static-access")
	public void setAsNormal(Player p, String arena){
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		team.removePlayer(p);
		Location loc = (Location) plugin.configLoc.get("Arenas." + arena+"." + "Saida." + ".Location");
		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
		p.teleport(loc);
		
	}

}
