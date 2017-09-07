package tedo.TapToDo;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class TCommand extends Command{

	public TCommand() {
		super("t", "TapToDoコマンド");

		this.setPermission("op");

		this.getCommandParameters().clear();
		this.addCommandParameters("default", new CommandParameter[]{
				new CommandParameter("add", false, new String[]{"add"}),
				new CommandParameter("コマンド", CommandParameter.ARG_TYPE_RAW_TEXT, false),
		});
		this.addCommandParameters("del", new CommandParameter[]{
				new CommandParameter("del", false, new String[]{"del"}),
				new CommandParameter("コマンド", CommandParameter.ARG_TYPE_RAW_TEXT, false),
		});
		this.addCommandParameters("delall", new CommandParameter[]{
				new CommandParameter("delall", false, new String[]{"delall"}),
		});
		this.addCommandParameters("list", new CommandParameter[]{
				new CommandParameter("list", false, new String[]{"list"}),
		});
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§a>>§bコンソールからこのコマンドを実行できません");
			return false;
		}
		Player player = (Player) sender;
		String commands = "";
		for (String command : args) {
			commands = commands + " " + command;
		}
		switch (args[0]) {
			case "add":
				commands = commands.replaceFirst(" add ", "");
				Main.main.status.put(player, "add");
				Main.main.check.put(player, commands);
				player.sendMessage("§a>>§bコマンドを設定するブロックをタップしてください");
				return true;

			case "del":
				commands = commands.replaceFirst(" del ", "");
				Main.main.status.put(player, "del");
				Main.main.check.put(player, commands);
				player.sendMessage("§a>>§bコマンドを削除するブロックをタップしてください");
				return true;

			case "delall":
				Main.main.status.put(player, "delall");
				player.sendMessage("§a>>§bコマンドをすべて削除するブロックをタップしてください");
				return true;

			case "list":
				Main.main.status.put(player, "list");
				player.sendMessage("§a>>§bコマンドを確認するブロックをタップしてください");
				return true;
		}
		return false;
	}
}
