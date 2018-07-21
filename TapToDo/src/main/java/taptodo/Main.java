package taptodo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class Main extends PluginBase implements Listener {

	public static Main main;

	public Config config;
	public HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

	public HashMap<Player, String> check = new HashMap<Player, String>();
	public HashMap<Player, String> status = new HashMap<Player, String>();

	@SuppressWarnings("unchecked")
	public void onEnable() {
		main = this;

		getServer().getCommandMap().register("TapToDo", new TCommand());

		getServer().getPluginManager().registerEvents(new PluginListener(), this);

		config = new Config(new File(getDataFolder(), "config.yml"),Config.YAML);
		config.getAll().forEach((pos, commands) -> {
			data.put(pos, (ArrayList<String>) commands);
		});
	}

	public void onSet(String pos, String command) {
		ArrayList<String> commands;
		if (data.containsKey(pos)) {
			commands = data.get(pos);
		} else {
			commands = new ArrayList<String>();
		}
		commands.add(command);
		data.put(pos, commands);
		config.set(pos, commands);
		config.save();
	}

	public boolean onRemove(String pos, String command) {
		if (data.containsKey(pos)) {
			ArrayList<String> commands = data.get(pos);
			if (commands.contains(command)) {
				commands.remove(command);
			}
			if (commands.size() > 0) {
				data.put(pos, commands);
				config.set(pos, commands);
				config.save();
				return true;
			} else {
				data.remove(pos);
				config.remove(pos);
				config.save();
				return true;
			}
		}
		return false;
	}

	public boolean onRemoveAll(String pos) {
		if (data.containsKey(pos)) {
			data.remove(pos);
			config.remove(pos);
			config.save();
			return true;
		}
		return false;
	}

	public boolean hasCommand(String pos) {
		return data.containsKey(pos);
	}
}
