package org.cyclops.evilcraft.core.broom;

import lombok.Data;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.IBroomPart;

/**
 * Base implementation for broom parts.
 * @author rubensworks
 */
@Data
public class BroomPartBase implements IBroomPart {

    private final BroomPartType type;

    public BroomPartBase(BroomPartType type) {
        this.type = type;
        if(MinecraftHelpers.isClientSide()) {
            registerModelResourceLocation();
        }
    }

    protected String getModId() {
        return Reference.MOD_ID;
    }

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getModId() + ":broomPart/" + getType().name().toLowerCase()));
    }
}
