package com.asestefan.mutationcraft.entity;

import software.bernie.geckolib.animatable.GeoEntity;

public interface AnimatedMutant extends GeoEntity {
    String getTexture();

    ProcedureAnimation procedureAnimation();

    default void playAnimation(String animation) {
        procedureAnimation().request(animation);
    }

    default void syncAnimation() {
        procedureAnimation().sync();
    }
}
