package taptodo;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class TCommand extends Command {

	public TCommand() {
		super("t", "TapToDo command");

		setPermission("op");

		getCommandParameters().clear();
		addCommandParameters("default", new CommandParameter[]{
				new CommandParameter("add", false, new String[]{"add"}),
				new CommandParameter("コマンド", CommandParameter.ARG_TYPE_RAW_TEXT, false),
		});
		addCommandParameters("del", new CommandParameter[]{
				new CommandParameter("del", false, new String[]{"del"}),
				new CommandParameter("コマンド", CommandParameter.ARG_TYPE_RAW_TEXT, false),
		});
		addCommandParameters("delall", new CommandParameter[]{
				new CommandParameter("delall", false, new String[]{"delall"}),
		});
		addCommandParameters("list", new CommandParameter[]{
				new CommandParameter("list", false, new String[]{"list"}),
		});
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("\u00A7cThis command works only ingame");
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
				player.sendMessage("\u00A76Tap block to add command");
				return true;

			case "del":
				commands = commands.replaceFirst(" del ", "");
				Main.main.status.put(player, "del");
				Main.main.check.put(player, commands);
				player.sendMessage("\u00A76Tap block to remove command");
				return true;

			case "delall":
				Main.main.status.put(player, "delall");
				player.sendMessage("\u00A76");
				return true;

			case "list":
				Main.main.status.put(player, "list");
				player.sendMessage("\u00A76");
				return true;
		}
		return false;
	}
}
