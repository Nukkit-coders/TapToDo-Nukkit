package tedo.TapToDo;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

public class PluginListener implements Listener{

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (block instanceof BlockAir) {
			return;
		}
		String pos = ((int) block.x) + ":" + ((int) block.y) + ":" + ((int) block.z);
		Player player = event.getPlayer();
		Main main = Main.main;
		if (main.status.containsKey(player)) {
			switch (main.status.get(player)) {
				case "add":
					main.onSet(pos, main.check.get(player));
					player.sendMessage("§a>>§bコマンドを設定しました");
					main.check.remove(player);
					break;
				case "del":
					if (main.onRemove(pos, main.check.get(player))) {
						player.sendMessage("§a>>§bコマンドを削除しました");
					} else {
						player.sendMessage("§a>>§bそのコマンドは設定されていません");
					}
					main.check.remove(player);
					break;
				case "delall":
					if (main.onRemoveAll(pos)) {
						player.sendMessage("§a>>§bコマンドを削除しました");
					} else {
						player.sendMessage("§a>>§bこのブロックにはコマンドが設定されていません");
					}
					break;
				case "list":
					if (!main.data.containsKey(pos)) {
						player.sendMessage("§a>>§bこのブロックにはコマンドがありません");
					} else {
						player.sendMessage("§a>>§bこのブロックのコマンド");
						for (String command : main.data.get(pos)) {
							player.sendMessage("§a>>§b" + command);
						}
					}
					break;
			}
			main.status.remove(player);
			event.setCancelled();
			return;
		}
		if (main.hasCommand(pos)) {
			ArrayList<String> commands = main.data.get(pos);
			for (String command : commands) {
				PlayerCommandPreprocessEvent ev = new PlayerCommandPreprocessEvent(player, command);
				player.getServer().getPluginManager().callEvent(ev);
				if (ev.isCancelled()) {
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
			event.setCancelled();
		}
	}
}
