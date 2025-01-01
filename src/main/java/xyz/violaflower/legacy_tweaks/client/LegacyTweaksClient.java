package xyz.violaflower.legacy_tweaks.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import xyz.violaflower.legacy_tweaks.LegacyTweaks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LegacyTweaksClient {
	public static void init() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			private final LegacyTweaksResourceManager legacyTweaksResourceManager = new LegacyTweaksResourceManager();
			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(LegacyTweaks.MOD_ID, "reload");
			}

			@Override
			public String getName() {
				return legacyTweaksResourceManager.getName();
			}

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
				return legacyTweaksResourceManager.reload(preparationBarrier, resourceManager, profilerFiller,profilerFiller2, executor, executor2);
			}
		});
	}
}