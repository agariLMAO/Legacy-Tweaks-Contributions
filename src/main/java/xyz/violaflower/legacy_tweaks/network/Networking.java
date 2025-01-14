package xyz.violaflower.legacy_tweaks.network;

import net.minecraft.network.chat.Component;
import xyz.violaflower.legacy_tweaks.network.client.ClientNetworking;
import xyz.violaflower.legacy_tweaks.network.payload.*;
import xyz.violaflower.legacy_tweaks.network.task.SendCoolConfigurationPacketTask;
import xyz.violaflower.legacy_tweaks.network.task.SendTweakStatesTask;
import xyz.violaflower.legacy_tweaks.networking.NetworkingAbstractions;
import xyz.violaflower.legacy_tweaks.networking.configuration.ServerboundConfiguration;
import xyz.violaflower.legacy_tweaks.networking.configuration.event.ServerConfigurationEvents;
import xyz.violaflower.legacy_tweaks.networking.play.ServerboundPlay;
import xyz.violaflower.legacy_tweaks.util.common.lang.Lang;

public class Networking {
	public static void registerNetworkingCodecs() {
		NetworkingAbstractions.registerCodec(CoolPacket.TYPE, CoolPacket.STREAM_CODEC, NetworkingAbstractions.PayloadType.PLAY_S2C);
		NetworkingAbstractions.registerCodec(CoolPacket2.TYPE, CoolPacket2.STREAM_CODEC, NetworkingAbstractions.PayloadType.PLAY_C2S);
		NetworkingAbstractions.registerCodec(TweakStatesPayload.TYPE, TweakStatesPayload.STREAM_CODEC, NetworkingAbstractions.PayloadType.PLAY_S2C);
		NetworkingAbstractions.registerCodec(TweakStatesResponsePayload.TYPE, TweakStatesResponsePayload.STREAM_CODEC, NetworkingAbstractions.PayloadType.PLAY_C2S);

		NetworkingAbstractions.registerCodec(CoolConfigurationPacket.TYPE, CoolConfigurationPacket.STREAM_CODEC, NetworkingAbstractions.PayloadType.CONFIGURATION_S2C);
		NetworkingAbstractions.registerCodec(TweakStatesPayload.TYPE, TweakStatesPayload.STREAM_CODEC, NetworkingAbstractions.PayloadType.CONFIGURATION_S2C);
		NetworkingAbstractions.registerCodec(TweakStatesResponsePayload.TYPE, TweakStatesResponsePayload.STREAM_CODEC, NetworkingAbstractions.PayloadType.CONFIGURATION_C2S);
		NetworkingAbstractions.registerCodec(CoolConfigurationResponsePacket.TYPE, CoolConfigurationResponsePacket.STREAM_CODEC, NetworkingAbstractions.PayloadType.CONFIGURATION_C2S);
	}

	/// @see ClientNetworking#registerPayloadHandlers()
	public static void registerPayloadHandlers() {
		ServerboundPlay.registerHandler(CoolPacket2.TYPE, (payload, context) -> {
			System.out.println("[SERVER] received packet " + payload);
		});
		ServerboundConfiguration.registerHandler(CoolConfigurationResponsePacket.TYPE, (payload, context) -> {
			//MinecraftServer server = context.;
			context.getServer().getPlayerList().broadcastSystemMessage(Component.literal(Lang.Networking.SERVER_RECEIVED.getString() + payload + Lang.Networking.FROM_CONNECT_CLIENT.getString()), true);
			context.finishCurrentTask(SendCoolConfigurationPacketTask.TYPE);
		});

		ServerboundConfiguration.registerHandler(TweakStatesResponsePayload.TYPE, (payload, context) -> {
			System.out.println("Client finished configuring tweaks.");
			context.finishCurrentTask(SendTweakStatesTask.TYPE);
		});

		ServerboundPlay.registerHandler(TweakStatesResponsePayload.TYPE, (payload, context) -> {
			System.out.println("Client finished configuring tweaks during play.");
		});
	}

	/// @see ClientNetworking#registerConfigurationTasks()
	public static void registerConfigurationTasks() {
		ServerConfigurationEvents.registerOnConfigure(h -> {
			h.addTask(new SendCoolConfigurationPacketTask());
			h.addTask(new SendTweakStatesTask());
		});
	}
}
