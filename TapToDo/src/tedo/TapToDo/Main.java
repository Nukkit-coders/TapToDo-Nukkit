package tedo.TapToDo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class Main extends PluginBase implements Listener{

	public static Main main;

	public Config config;
	public HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

	public HashMap<Player, String> check = new HashMap<Player, String>();
	public HashMap<Player, String> status = new HashMap<Player, String>();

	@SuppressWarnings("unchecked")
	public void onEnable() {
		main = this;

		this.getServer().getCommandMap().register("TapToDo", new TCommand());

		this.getServer().getPluginManager().registerEvents(new PluginListener(), this);

		this.config = new Config(new File(this.getDataFolder(), "config.yml"),Config.YAML);
		this.config.getAll().forEach((pos, commands) -> {
			this.data.put(pos, (ArrayList<String>) commands);
		});
	}

	public void onSet(String pos, String command) {
		ArrayList<String> commands;
		if (this.data.containsKey(pos)) {
			commands = this.data.get(pos);
		} else {
			commands = new ArrayList<String>();
		}
		commands.add(command);
		this.data.put(pos, commands);
		this.config.set(pos, commands);
		this.config.save();
	}

	public boolean onRemove(String pos, String command) {
		if (this.data.containsKey(pos)) {
			ArrayList<String> commands = this.data.get(pos);
			if (commands.contains(command)) {
				commands.remove(command);
			}
			if (commands.size() > 0) {
				this.data.put(pos, commands);
				this.config.set(pos, commands);
				this.config.save();
				return true;
			} else {
				this.data.remove(pos);
				this.config.remove(pos);
				this.config.save();
				return true;
			}
		}
		return false;
	}

	public boolean onRemoveAll(String pos) {
		if (this.data.containsKey(pos)) {
			this.data.remove(pos);
			this.config.remove(pos);
			this.config.save();
			return true;
		}
		return false;
	}

	public boolean hasCommand(String pos) {
		return this.data.containsKey(pos);
	}
}
