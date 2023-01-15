package org.cyclops.evilcraft.proxy;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.key.ExaltedCrafterKeyHandler;
import org.cyclops.evilcraft.client.key.FartKeyHandler;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChaliceBaked;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBoxOfEternalClosure;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBroom;
import org.cyclops.evilcraft.core.client.model.ModelLoaderBroomPart;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDarkTank;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDisplayStand;
import org.cyclops.evilcraft.core.client.model.ModelLoaderEntangledChalice;
import org.cyclops.evilcraft.event.RenderOverlayEventHook;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxy extends ClientProxyComponent {

    public static ModelBakery modelBakery;

    public ClientProxy() {
        super(new CommonProxy());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBakingCompleted);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelRegisterAdditional);
    }

    @Override
    public ModBase getMod() {
        return EvilCraft._instance;
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event) {
        Options settings = Minecraft.getInstance().options;

        for (KeyMapping key : Keys.KEYS)
            event.register(key);

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

        MinecraftForge.EVENT_BUS.register(new RenderOverlayEventHook());
    }

    public void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
        this.modelBakery = event.getModelBakery();
    }

    public void onModelLoad(ModelEvent.RegisterGeometryLoaders event) {
        event.register("broom", new ModelLoaderBroom());
        event.register("broom_part", new ModelLoaderBroomPart());
        event.register("box_of_eternal_closure", new ModelLoaderBoxOfEternalClosure());
        event.register("dark_tank", new ModelLoaderDarkTank());
        event.register("entangled_chalice", new ModelLoaderEntangledChalice());
        event.register("display_stand", new ModelLoaderDisplayStand());
    }

    public void onModelRegisterAdditional(ModelEvent.RegisterAdditional event) {
        // Box of eternal closure
        event.register(ModelBoxOfEternalClosure.boxModel);
        event.register(ModelBoxOfEternalClosure.boxLidModel);
        event.register(ModelBoxOfEternalClosure.boxLidRotatedModel);
        // Broom
        for (ResourceLocation partModel : BroomParts.REGISTRY.getPartModels()) {
            event.register(partModel);
        }
        // Entangled chalice
        event.register(ModelEntangledChaliceBaked.chaliceModelName);
        event.register(ModelEntangledChaliceBaked.gemsModelName);
    }

}
