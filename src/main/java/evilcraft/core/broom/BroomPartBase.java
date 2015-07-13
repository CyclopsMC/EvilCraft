package evilcraft.core.broom;

import evilcraft.Reference;
import evilcraft.api.broom.IBroomPart;
import lombok.Data;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
                new ModelResourceLocation(getModId() + ":broomPart/" + getType().name().toLowerCase()));
    }
}
