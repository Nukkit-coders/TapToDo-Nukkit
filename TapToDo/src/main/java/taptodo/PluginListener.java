package taptodo;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

public class PluginListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev) {
		Block block = ev.getBlock();
		if (block instanceof BlockAir) {
			return;
		}
		String pos = ((int) block.x) + ":" + ((int) block.y) + ":" + ((int) block.z);
		Player player = ev.getPlayer();
		Main main = Main.main;
		if (main.status.containsKey(player)) {
			switch (main.status.get(player)) {
				case "add":
					main.onSet(pos, main.check.get(player));
					player.sendMessage("\u00A76Command added");
					main.check.remove(player);
					break;
				case "del":
					if (main.onRemove(pos, main.check.get(player))) {
						player.sendMessage("\u00A76Remove command");
					} else {
						player.sendMessage("\u00A76Command settings");
					}
					main.check.remove(player);
					break;
				case "delall":
					if (main.onRemoveAll(pos)) {
						player.sendMessage("\u00A76Remove all commands");
					} else {
						player.sendMessage("\u00A76Command settings");
					}
					break;
				case "list":
					if (!main.data.containsKey(pos)) {
						player.sendMessage("\u00A76Commans settings");
					} else {
						player.sendMessage("\u00A76Block's commands");
						for (String command : main.data.get(pos)) {
							player.sendMessage("\u00A76" + command);
						}
					}
					break;
			}
			main.status.remove(player);
			ev.setCancelled();
			return;
		}
		if (main.hasCommand(pos)) {
			ArrayList<String> commands = main.data.get(pos);
			for (String command : commands) {
				PlayerCommandPreprocessEvent cev = new PlayerCommandPreprocessEvent(player, command);
				player.getServer().getPluginManager().callEvent(cev);
				if (cev.isCancelled()) {
					return;
				}
				if (player.isOp()) {
					command.replaceAll(" @op", "");
					command = command.replaceAll("@p", player.getName());
					if (command.indexOf("@a") != -1) {
						for (Player target : main.getServer().getOnlinePlayers().values()) {
							String cm = command.replaceFirst("@a", target.getName());
							player.getServer().dispatchCommand(player, cm);
						}
					} else {
						player.getServer().dispatchCommand(player, command);
					}
				} else {
					boolean r = command.indexOf(" @op") != -1;
					if (r) {
						command.replaceAll(" @op", "");
						player.getServer().addOp(player.getName().toLowerCase());
					}
					command = command.replaceAll("@p", player.getName());
					if (command.indexOf("@a") != -1) {
						for (Player target : main.getServer().getOnlinePlayers().values()) {
							String cm = command.replaceFirst("@a", target.getName());
							player.getServer().dispatchCommand(player, cm);
						}
					} else {
						player.getServer().dispatchCommand(player, command);
					}
					if (r) {
						player.getServer().removeOp(player.getName().toLowerCase());
					}
				}
			}
			ev.setCancelled();
		}
	}
}
