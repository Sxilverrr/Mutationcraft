package com.asestefan.mutationcraft.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class FlamethrowerFlameParticle extends TextureSheetParticle {
    private static final double BOUNCE = 0.45;
    private static final double STILL = 1.0E-5;

    protected FlamethrowerFlameParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.friction = 0.96F;
        this.gravity = 0.0F;
        this.hasPhysics = true;
        this.quadSize *= 1.0F + this.random.nextFloat() * 0.4F;
        this.lifetime = 16 + this.random.nextInt(10);
    }

    @Override
    public void move(double dx, double dy, double dz) {
        Vec3 actual = Entity.collideBoundingBox(null, new Vec3(dx, dy, dz), this.getBoundingBox(), this.level, List.of());
        if (actual.lengthSqr() > 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(actual));
            this.setLocationFromBoundingbox();
        }
        this.onGround = dy < 0.0 && Math.abs(dy - actual.y) > STILL;
        if (Math.abs(dx - actual.x) > STILL) {
            this.xd = -dx * BOUNCE;
        }
        if (Math.abs(dy - actual.y) > STILL) {
            this.yd = -dy * BOUNCE;
        }
        if (Math.abs(dz - actual.z) > STILL) {
            this.zd = -dz * BOUNCE;
        }
    }

    @Override
    public float getQuadSize(float partialTick) {
        float age = (this.age + partialTick) / this.lifetime;
        return this.quadSize * (1.0F - age * age * 0.5F);
    }

    @Override
    public int getLightColor(float partialTick) {
        float age = Mth.clamp((this.age + partialTick) / this.lifetime, 0.0F, 1.0F);
        int light = super.getLightColor(partialTick);
        int block = light & 255;
        int sky = light >> 16 & 255;
        block += (int) (age * 15.0F * 16.0F);
        if (block > 240) {
            block = 240;
        }
        return block | sky << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            FlamethrowerFlameParticle particle = new FlamethrowerFlameParticle(level, x, y, z, vx, vy, vz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
