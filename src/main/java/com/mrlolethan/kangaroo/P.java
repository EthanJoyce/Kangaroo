package com.mrlolethan.kangaroo;

import java.io.File;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

import com.mrlolethan.kangaroo.module.Module;
import com.mrlolethan.kangaroo.module.ModuleFileFlip;

public class P extends JavaPlugin {

	@Getter private ConfigManager configManager;

	private Module loadedModule = new ModuleFileFlip();


	@Override
	public void onLoad() {
		this.configManager = new ConfigManager(this.getConfig(), new File(this.getDataFolder(), "config.yml"));
		this.getConfigManager().createDefaults();

		P.log(Level.INFO, "Performing update...");
		this.loadedModule.performUpdate();
		P.log(Level.INFO, "Update complete!");
	}


	public static void log(Level level, String msg) {
		P.i().getLogger().log(level, msg);
	}


	/*
	 * Singleton boiler plate
	 */
	private static P instance;

	{
		instance = this;
	}

	public static P i() {
		return instance;
	}

}
