package com.mrlolethan.kangaroo.module;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;

import com.mrlolethan.kangaroo.P;

/**
 * <p>The standard update module.</p>
 * 
 * <p>
 * This module <b>recursively</b> finds all files (located in the configurable root directory) ending with ".new," and
 *   replaces them with their counterpart.
 *  </p>
 *  <p>
 *  For example, if I had two files name "MyPlugin.jar" and "MyPlugin.jar.new," when this module's update
 *   procedure is invoked, "MyPlugin.jar" would be replaced with "MyPlugin.jar.new." If no counterpart
 *   exists, then "MyPlugin.jar.new" will simply be renamed to "MyPlugin.jar."
 *  </p>
 *  <p>
 *  This is a simple way of enabling the staging of file modifications, allowing them to propagate when the
 *   server (re)starts.
 *  </p>
 *  <p>Note: the ".new" extension will be removed from the staged file(s). That is, "MyPlugin.jar.new" will become "MyPlugin.jar."
 */
public class ModuleFileFlip implements Module {

	/** The number of files that have been flipped (for this update session). */
	@Getter private int flipCount = 0;

	@Override
	public void performUpdate() {
		final File rootDir = P.i().getConfigManager().getRootDir();
		this.recursiveFlip(rootDir);

		P.log(Level.INFO, "Files flipped (for this session): " + this.getFlipCount());
	}

	private void recursiveFlip(final File file) {
		if (file == null) {
			P.log(Level.WARNING, "Tried to flip a null file.");
			return; // We don't want null files.
		}

		if (file.isFile() && file.getName().toLowerCase().endsWith(".new")) {
			// This file should be flipped
			File parent = file.getParentFile();
			File old = new File(parent, file.getName().substring(0, file.getName().length() - 4));

			if (old.exists()) { 
				old.delete();
			}
			file.renameTo(old);
			this.checkLoadPluginArchive(old);

			// Increment the flip count
			this.flipCount++;
		} else if (file.isDirectory()) {
			// This is a directory; let's iterate through all children and try to flip them (recursively)
			for (File child : file.listFiles()) {
				this.recursiveFlip(child);
			}
		}
	}

	private void checkLoadPluginArchive(File file) {
		String fileParentCpath = null;
		String pluginsDirCpath = null;

		try {
			fileParentCpath = file.getParentFile().getCanonicalPath();
			pluginsDirCpath = P.i().getDataFolder().getParentFile().getCanonicalPath();
		} catch (IOException ignored) { }

		// Is this file actually a plugin archive?
		if (file.getName().endsWith(".jar") && fileParentCpath.equals(pluginsDirCpath)) {
			try {
				// Yes, load 'er up
				Bukkit.getPluginManager().loadPlugin(file);
			} catch (InvalidDescriptionException | InvalidPluginException | UnknownDependencyException ex) {
				P.log(Level.WARNING, "Exception raised whilst loading plugin: " + ex.getLocalizedMessage());
			}
		}
	}

}
