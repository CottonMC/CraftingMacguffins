package io.github.cottonmc.craftingmacguffins;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.craftingmacguffins.config.Macguffins;
import io.github.cottonmc.craftingmacguffins.data.MacguffinReloadManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class CraftingMacguffins implements ModInitializer {

	public static final String MOD_ID = "crafting-macguffins";

	public static ModLogger LOGGER = new ModLogger(MOD_ID, "Crafting Macguffins");
	public static Macguffins config;

	@Override
	public void onInitialize() {
		LOGGER.setPrefixFormat(Ansi.Yellow);
		config = ConfigManager.loadConfig(Macguffins.class);
		ResourceManagerHelper.get(ResourceType.DATA).registerReloadListener(new MacguffinReloadManager());

	}
}
