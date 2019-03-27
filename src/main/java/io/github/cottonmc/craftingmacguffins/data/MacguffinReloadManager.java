package io.github.cottonmc.craftingmacguffins.data;

import io.github.cottonmc.craftingmacguffins.CraftingMacguffins;
import io.github.cottonmc.craftingmacguffins.config.Macguffins;
import io.github.cottonmc.repackage.blue.endless.jankson.Jankson;
import io.github.cottonmc.repackage.blue.endless.jankson.JsonObject;
import io.github.cottonmc.repackage.blue.endless.jankson.impl.SyntaxError;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MacguffinReloadManager implements SimpleResourceReloadListener {

	public static Map<Identifier, Item> MACGUFFINS = new HashMap<>();

	@Override
	public CompletableFuture load(net.minecraft.resource.ResourceManager manager, net.minecraft.util.profiler.Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Jankson jankson = Jankson.builder().build();
			String path = "config/crafting-macguffins";
			Collection<Identifier> resources = manager.findResources(path, (name) -> name.equals("macguffins.json"));
			if (resources.size() == 0) CraftingMacguffins.LOGGER.error("Couldn't find any macguffin entries!");
			for (Identifier fileId : resources) {
				try {
					for (Resource res : manager.getAllResources(fileId)) {
						JsonObject json = jankson.load(IOUtils.toString(res.getInputStream()));
						Macguffins macguffins = jankson.fromJson(json, Macguffins.class);
						for (int i = 0; i < macguffins.magicMacguffins; i++) {
							MACGUFFINS.putIfAbsent(new Identifier(CraftingMacguffins.MOD_ID, DyeColor.byId(i).getName()+"_slate"), new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
						}
						for (int i = 0; i < macguffins.industrialMacguffins; i++) {
							MACGUFFINS.putIfAbsent(new Identifier(CraftingMacguffins.MOD_ID, DyeColor.byId(i).getName()+"_cog"), new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
						}
						for (int i = 0; i < macguffins.sciFiMacguffins; i++) {
							MACGUFFINS.putIfAbsent(new Identifier(CraftingMacguffins.MOD_ID, DyeColor.byId(i).getName()+"_transistor"), new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
						}
					}
				} catch (IOException | SyntaxError | NullPointerException e) {
					CraftingMacguffins.LOGGER.error("Couldn't load macguffins: %s", e);
				}
			}
			return MACGUFFINS;
		}, executor);
	}

	@Override
	public CompletableFuture<Void> apply(Object data, net.minecraft.resource.ResourceManager manager, net.minecraft.util.profiler.Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			for (Identifier id : MACGUFFINS.keySet()) {
				System.out.println("Added macguffin!");
				if (!Registry.ITEM.containsId(id)) Registry.register(Registry.ITEM, id, MACGUFFINS.get(id));
			}
		});
	}

	@Override
	public net.minecraft.util.Identifier getFabricId() {
		return new Identifier(CraftingMacguffins.MOD_ID, "macguffins");
	}
}
