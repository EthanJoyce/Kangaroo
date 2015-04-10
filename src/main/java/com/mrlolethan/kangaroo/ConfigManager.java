package com.mrlolethan.kangaroo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.bukkit.configuration.file.FileConfiguration;

@RequiredArgsConstructor
public class ConfigManager {

	@NonNull private FileConfiguration config;
	@NonNull private File configFile;

	/*
	 * Config nodes
	 */
	/** The root directory in which to recursively perform the deployment. Defaults to the server's root directory. */
	@Getter private File rootDir = new File(".");


	public void createDefaults() {
		this.config.options().header("Refer to the Kangaroo wiki for details: https://github.com/mrlolethan/Kangaroo/wiki");

		this.config.addDefault("RootDir", this.getRootDir().getPath());

		this.config.options().copyDefaults(true);
		try {
			this.config.save(this.configFile);
		} catch (IOException ex) {
			P.log(Level.WARNING, "IOException thrown whilst attempting to save the default configuration file: " + ex.getLocalizedMessage());
		}
	}

}
