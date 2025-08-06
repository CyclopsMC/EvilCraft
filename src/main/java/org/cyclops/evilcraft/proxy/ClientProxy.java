package org.cyclops.evilcraft.proxy;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;
import org.cyclops.evilcraft.client.key.ExaltedCrafterKeyHandler;
import org.cyclops.evilcraft.client.key.FartKeyHandler;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.client.render.blockentity.*;
import org.cyclops.evilcraft.core.client.model.*;
import org.cyclops.evilcraft.event.RenderOverlayEventHook;

import java.lang.reflect.Field;

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
    }

    @Override
    public ModBaseNeoForge<EvilCraft> getMod() {
        return EvilCraft._instance;
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event) {
        Options settings;
        try {
            Field field = RegisterKeyMappingsEvent.class.getDeclaredField("options");
            field.setAccessible(true);
            settings = (Options) field.get(event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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

    @Override
    public void registerRenderers() {
        registerRenderer(RegistryEntries.BLOCK_ENTITY_BLOOD_CHEST.get(), RenderBlockEntityBloodChest::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_BOX_OF_ETERNAL_CLOSURE.get(), RenderBlockEntityBoxOfEternalClosure::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_COLOSSAL_BLOOD_CHEST.get(), RenderBlockEntityColossalBloodChest::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_DARK_TANK.get(), RenderBlockEntityDarkTank::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND.get(), RenderBlockEntityDisplayStand::new);
        if (!BlockEntangledChaliceConfig.staticBlockRendering) {
            registerRenderer(RegistryEntries.BLOCK_ENTITY_ENTANGLED_CHALICE.get(), RenderBlockEntityEntangledChalice::new);
        }
        registerRenderer(RegistryEntries.BLOCK_ENTITY_ENVIRONMENTAL_ACCUMULATOR.get(), RenderBlockEntityEnvironmentalAccumulator::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_PURIFIER.get(), RenderBlockEntityPurifier::new);
        registerRenderer(RegistryEntries.BLOCK_ENTITY_SPIRIT_PORTAL.get(), RenderBlockEntitySpiritPortal::new);

        super.registerRenderers();
    }

    public void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
        this.modelBakery = event.getModelBakery();
    }

    public void onModelLoad(ModelEvent.RegisterLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "broom"), new ModelLoaderBroom());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "broom_part"), new ModelLoaderBroomPart());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "box_of_eternal_closure"), new ModelLoaderBoxOfEternalClosure());
        event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entangled_chalice"), new ModelLoaderEntangledChalice());
        event.register(ModelLoaderDisplayStand.ID, ModelLoaderDisplayStand.getInstance());
    }

}
