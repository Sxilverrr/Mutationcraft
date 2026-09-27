package com.asestefan.mutationcraft.behavior;

import net.minecraft.world.entity.LivingEntity;
//? if >=1.21 {
/*import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?}

public final class AnimalSwingAnimation {
    private final LivingEntity entity;
    private final RawAnimation animation;
    private boolean swinging;
    private long lastSwing;

    public AnimalSwingAnimation(LivingEntity entity, String animation) {
        this.entity = entity;
        this.animation = RawAnimation.begin().thenPlay(animation);
    }

    public PlayState predicate(AnimationState<?> event) {
        long time = this.entity.level().getGameTime();
        if (this.entity.getAttackAnim(event.getPartialTick()) > 0.0F && !this.swinging) {
            this.swinging = true;
            this.lastSwing = time;
        }
        if (this.swinging && this.lastSwing + 15L <= time) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(this.animation);
        }
        return PlayState.CONTINUE;
    }
}
