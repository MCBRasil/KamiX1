package me.guigarciazinho.comandos;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.guigarciazinho.principal.Main;

public class Local implements CommandExecutor {
	private Main plugin;
	private String prefixo;
	
	public Local(Main main){
		plugin = main;
		prefixo = plugin.getConfig().getString("Prefixo").replace("&", "§");
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("setx1")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("x1.admin")){
				if (args.length >= 2) {
					if("loc1".equalsIgnoreCase(args[0])){
						plugin.configLoc.set("Arenas." + args[1]+"." + "Loc1." + ".Location", p.getLocation());
						plugin.configLoc.set("Arenas." + args[1]+"." + ".Nome", args[1]);
						p.sendMessage("[§eKamiX1§f]§2 Entrada1 definida com sucesso!");
						try {
							plugin.configLoc.save(plugin.configFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					
					}
					if("loc2".equalsIgnoreCase(args[0])){
						plugin.configLoc.set("Arenas." + args[1]+"." + "Loc2." + ".Location", p.getLocation());
						p.sendMessage("[§eKamiX1§f]§2 Entrada2 definida com sucesso!");
						try {
							plugin.configLoc.save(plugin.configFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if("camarote".equalsIgnoreCase(args[0])){
						plugin.configLoc.set("Arenas." + args[1]+"." + "Camarote." + ".Location", p.getLocation());
						p.sendMessage("[§eKamiX1§f]§2 Camarote definido com sucesso!");
						try {
							plugin.configLoc.save(plugin.configFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if("saida".equalsIgnoreCase(args[0])){
						plugin.configLoc.set("Arenas." + args[1]+"." + "Saida." + ".Location", p.getLocation());
						p.sendMessage("[§eKamiX1§f]§2 Saida definida com sucesso!");
						try {
							plugin.configLoc.save(plugin.configFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else {
					sender.sendMessage("[§eKamiX1§f]§c Você utilizou o comando de forma errada.");
					return true;
				}

				}else{
					sender.sendMessage(prefixo + plugin.game.getConfigString("Sem permissao loc"));
				}
			}else{
				sender.sendMessage("[§eKamiX1§f]§c Comando apenas para Jogadores");
				return true;
			}
		}
		return false;
	}

}
