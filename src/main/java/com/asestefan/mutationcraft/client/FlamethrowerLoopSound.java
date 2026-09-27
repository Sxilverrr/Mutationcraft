package com.asestefan.mutationcraft.client;

import java.util.function.BooleanSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class FlamethrowerLoopSound extends AbstractTickableSoundInstance {
    private static final float FULL_VOLUME = 1.0F;
    private static final float FADE_STEP = 0.34F;
    private final Entity source;
    private final BooleanSupplier active;
    private boolean fading;

    public FlamethrowerLoopSound(SoundEvent sound, SoundSource category, Entity source, BooleanSupplier active) {
        super(sound, category, SoundInstance.createUnseededRandom());
        this.source = source;
        this.active = active;
        this.looping = true;
        this.delay = 0;
        this.volume = FULL_VOLUME;
        this.x = source.getX();
        this.y = source.getY();
        this.z = source.getZ();
    }

    public boolean isFading() {
        return this.fading;
    }

    @Override
    public void tick() {
        if (this.source.isRemoved()) {
            this.stop();
            return;
        }
        this.x = this.source.getX();
        this.y = this.source.getY();
        this.z = this.source.getZ();
        if (!this.fading && !this.active.getAsBoolean()) {
            this.fading = true;
        }
        if (this.fading) {
            this.volume -= FADE_STEP;
            if (this.volume <= 0.0F) {
                this.stop();
            }
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
