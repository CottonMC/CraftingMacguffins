package io.github.cottonmc.craftingmacguffins.config;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import io.github.cottonmc.repackage.blue.endless.jankson.Comment;

@ConfigFile(name = "macguffins")
public class Macguffins {

	@Comment("How many magic-themed macguffins to add, from 0 to 16.")
	public int magicMacguffins = 0;

	@Comment("How many sci-fi-themed macguffins to add, from 0 to 16.")
	public int sciFiMacguffins = 0;

	@Comment("Howmany industrial-themed macguffins to add, from 0 to 16.")
	public int industrialMacguffins = 0;

}
