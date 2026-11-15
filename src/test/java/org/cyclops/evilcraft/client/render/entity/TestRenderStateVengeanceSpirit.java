package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import org.junit.jupiter.api.Test;

/**
 * @author rubensworks
 */
public class TestRenderStateVengeanceSpirit {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void testUsableByLivingEntityModel() {
        // Other mods may render a spirit's extracted render state with a living entity model,
        // such as Ice and Fire's stone statues (#1305).
        Model model = new PigModel(PigModel.createBodyLayer(CubeDeformation.NONE).bakeRoot());
        model.setupAnim(new RenderStateVengeanceSpirit());
    }

}
