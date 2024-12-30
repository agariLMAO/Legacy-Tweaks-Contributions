package xyz.violaflower.legacy_tweaks.tweaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweakManager {
    public static final TweakManager INSTANCE = new TweakManager();
    public static TweakManager getInstance() {
        return INSTANCE;
    }

    public Map<String, Tweak> tweaks = new HashMap<>();

    public void register(Tweak tweak) {
        tweaks.put(tweak.getTweakID(), tweak);
        tweak.onRegister();
    }

    public Tweak getTweak(String id) {
        return tweaks.get(id);
    }

    public static final Codec<List<String>> TWEAKS = Codec.STRING.listOf();
    public static void save() {
        // get enabled tweaks and save them to a JSON?
        JsonElement jsonElement = TWEAKS.encodeStart(JsonOps.INSTANCE, getInstance().tweaks.entrySet().stream().filter(a -> a.getValue().isEnabled()).map(Map.Entry::getKey).toList()).resultOrPartial(System.err::println).orElseThrow();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonElement);
		try {
			Files.writeString(Platform.getConfigFolder().resolve("legacy-tweaks.json"), json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    public static void load() {

    }
}
