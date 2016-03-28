package org.cyclops.evilcraft.core.broom;

import lombok.Data;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;

/**
 * Base implementation for broom parts.
 * @author rubensworks
 */
@Data
public class BroomPartBase implements IBroomPart {

    private final ResourceLocation id;
    private final BroomPartType type;

    public BroomPartBase(ResourceLocation id, BroomPartType type) {
        this.id = id;
        this.type = type;
        if(MinecraftHelpers.isClientSide()) {
            registerModelResourceLocation();
        }
    }

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(id.getResourceDomain(), "broomPart/" + id.getResourcePath().toLowerCase()));
    }
}
