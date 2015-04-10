package com.mrlolethan.kangaroo.module;

/**
 * The update module interface.
 */
public interface Module {

	/**
	 * Perform the update. This shall only be run before all other plugins are loaded.
	 */
	public void performUpdate();

}
