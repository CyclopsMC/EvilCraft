package org.cyclops.evilcraft.proxy;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
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
        EvilCraft._instance.getModEventBus().addListener(this::onModelBakingCompleted);
        EvilCraft._instance.getModEventBus().addListener(this::onModelLoad);
        EvilCraft._instance.getModEventBus().addListener(this::onModelRegisterAdditional);
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

        NeoForge.EVENT_BUS.register(new RenderOverlayEventHook());
    }

    public void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
        this.modelBakery = event.getModelBakery();
    }

    public void onModelLoad(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "broom"), new ModelLoaderBroom());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "broom_part"), new ModelLoaderBroomPart());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "box_of_eternal_closure"), new ModelLoaderBoxOfEternalClosure());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dark_tank"), new ModelLoaderDarkTank());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entangled_chalice"), new ModelLoaderEntangledChalice());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "display_stand"), new ModelLoaderDisplayStand());
    }

    public void onModelRegisterAdditional(ModelEvent.RegisterAdditional event) {
        // Box of eternal closure
        event.register(ModelResourceLocation.standalone(ModelBoxOfEternalClosure.boxModel));
        event.register(ModelResourceLocation.standalone(ModelBoxOfEternalClosure.boxLidModel));
        event.register(ModelResourceLocation.standalone(ModelBoxOfEternalClosure.boxLidRotatedModel));
        // Broom
        for (ResourceLocation partModel : BroomParts.REGISTRY.getPartModels()) {
            event.register(ModelResourceLocation.standalone(partModel));
        }
        // Entangled chalice
        event.register(ModelResourceLocation.standalone(ModelEntangledChaliceBaked.chaliceModelName));
        event.register(ModelResourceLocation.standalone(ModelEntangledChaliceBaked.gemsModelName));
    }

}
