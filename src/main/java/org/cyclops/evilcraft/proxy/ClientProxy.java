package org.cyclops.evilcraft.proxy;

import net.minecraft.client.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.key.ExaltedCrafterKeyHandler;
import org.cyclops.evilcraft.client.key.FartKeyHandler;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityPurifier;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBoxOfEternalClosure;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBroom;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBroomPart;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDarkTank;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDisplayStand;
import org.cyclops.evilcraft.core.client.model.ModelLoaderEntangledChalice;
import org.cyclops.evilcraft.event.RenderOverlayEventHook;
import org.cyclops.evilcraft.event.TextureStitchEventHook;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends ClientProxyComponent {

	public ClientProxy() {
		super(new CommonProxy());
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPreTextureStitch);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelLoad);
	}

	@Override
	public ModBase getMod() {
		return EvilCraft._instance;
	}

	@Override
	public void registerKeyBindings(IKeyRegistry keyRegistry) {
		Options settings = Minecraft.getInstance().options;

		for (KeyMapping key : Keys.KEYS)
			ClientRegistry.registerKeyBinding(key);

		// Fart key
        FartKeyHandler fartKeyHandler = new FartKeyHandler();

        keyRegistry.addKeyHandler(Keys.FART, fartKeyHandler);
        keyRegistry.addKeyHandler(Keys.EXALTEDCRAFTING, new ExaltedCrafterKeyHandler());
        keyRegistry.addKeyHandler(settings.keyShift, fartKeyHandler);

		EvilCraft.clog("Registered key bindings");
	}

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());
		MinecraftForge.EVENT_BUS.register(new RenderOverlayEventHook());
	}

	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location().equals(RenderBlockEntityPurifier.TEXTURE_BLOOK.atlasLocation())) {
			event.addSprite(RenderBlockEntityPurifier.TEXTURE_BLOOK.texture());
		}
	}

	public void onModelLoad(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "broom"), new ModelLoaderBroom());
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "broom_part"), new ModelLoaderBroomPart());
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "box_of_eternal_closure"), new ModelLoaderBoxOfEternalClosure());
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "dark_tank"), new ModelLoaderDarkTank());
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "entangled_chalice"), new ModelLoaderEntangledChalice());
		ModelLoaderRegistry.registerLoader(new ResourceLocation(Reference.MOD_ID, "display_stand"), new ModelLoaderDisplayStand());
	}
    
}
