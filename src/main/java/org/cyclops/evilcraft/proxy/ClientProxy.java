package org.cyclops.evilcraft.proxy;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.key.ExaltedCrafterKeyHandler;
import org.cyclops.evilcraft.client.key.FartKeyHandler;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityPurifier;
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
		GameSettings settings = Minecraft.getInstance().gameSettings;

		for (KeyBinding key : Keys.KEYS)
			ClientRegistry.registerKeyBinding(key);

		// Fart key
        FartKeyHandler fartKeyHandler = new FartKeyHandler();

        keyRegistry.addKeyHandler(Keys.FART, fartKeyHandler);
        keyRegistry.addKeyHandler(Keys.EXALTEDCRAFTING, new ExaltedCrafterKeyHandler());
        keyRegistry.addKeyHandler(settings.keyBindSneak, fartKeyHandler);

		EvilCraft.clog("Registered key bindings");
	}

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());
		MinecraftForge.EVENT_BUS.register(new RenderOverlayEventHook());
	}

	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		if (event.getMap().getTextureLocation().equals(RenderTileEntityPurifier.TEXTURE_BLOOK.getAtlasLocation())) {
			event.addSprite(RenderTileEntityPurifier.TEXTURE_BLOOK.getTextureLocation());
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
