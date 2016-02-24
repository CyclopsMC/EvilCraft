package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.model.SingleModelLoader;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;

/**
 * Config for the {@link EntangledChalice}.
 * @author rubensworks
 *
 */
public class EntangledChaliceConfig extends BlockContainerConfig {

    @SideOnly(Side.CLIENT)
    public static ResourceLocation chaliceModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation gemsModel;

    /**
     * The unique instance.
     */
    public static EntangledChaliceConfig _instance;

    /**
     * Make a new instance.
     */
    public EntangledChaliceConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entangledChalice",
            null,
            EntangledChalice.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return EntangledChaliceItem.class;
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
            if (MinecraftHelpers.isClientSide()) {
                chaliceModel = new ResourceLocation(getMod().getModId() + ":block/chalice");
                gemsModel = new ResourceLocation(getMod().getModId() + ":block/gems");

                ModelEntangledChalice modelEntangledChalice = new ModelEntangledChalice();
                ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                        Reference.MOD_ID, "models/item/entangledChalice", modelEntangledChalice));
                ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                        Reference.MOD_ID, "models/block/entangledChalice", modelEntangledChalice));
            }
        }
    }
    
}
