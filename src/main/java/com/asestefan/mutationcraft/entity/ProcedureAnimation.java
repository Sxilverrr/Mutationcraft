package com.asestefan.mutationcraft.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
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

public final class ProcedureAnimation {
    public static final String EMPTY = "empty";
    public static final String UNDEFINED = "undefined";

    private final SynchedEntityData data;
    private final EntityDataAccessor<String> accessor;
    private String current = EMPTY;
    private String previous = EMPTY;

    public ProcedureAnimation(SynchedEntityData data, EntityDataAccessor<String> accessor) {
        this.data = data;
        this.accessor = accessor;
    }

    public void request(String animation) {
        this.data.set(this.accessor, animation);
    }

    public void sync() {
        String animation = this.data.get(this.accessor);
        if (!animation.equals(UNDEFINED)) {
            this.data.set(this.accessor, UNDEFINED);
            this.current = animation;
        }
    }

    public boolean isPlaying() {
        return !this.current.equals(EMPTY);
    }

    public <T extends AnimatedMutant> PlayState predicate(AnimationState<T> event) {
        AnimationController<T> controller = event.getController();
        if (this.isPlaying() && (controller.getAnimationState() == AnimationController.State.STOPPED || !this.current.equals(this.previous))) {
            if (!this.current.equals(this.previous)) {
                controller.forceAnimationReset();
            }
            controller.setAnimation(RawAnimation.begin().thenPlay(this.current));
            if (controller.getAnimationState() == AnimationController.State.STOPPED) {
                this.current = EMPTY;
                controller.forceAnimationReset();
            }
        } else if (!this.isPlaying()) {
            this.previous = EMPTY;
            return PlayState.STOP;
        }
        this.previous = this.current;
        return PlayState.CONTINUE;
    }
}
