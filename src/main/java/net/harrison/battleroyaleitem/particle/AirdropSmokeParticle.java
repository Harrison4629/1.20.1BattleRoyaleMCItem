package net.harrison.battleroyaleitem.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class AirdropSmokeParticle extends TextureSheetParticle {
    protected AirdropSmokeParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 1F;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.quadSize = 0.1F;
        this.lifetime = 200;
        this.setSpriteFromAge(sprites);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

    }

    @Override
    public void tick() {
        super.tick();
        //this.yd *= 0.95D;
        this.alpha = - ((float) 1 /lifetime) * age + 1;
        float progress = (float) this.age / this.lifetime;
        float currentSize;

        if (progress < 0.2F) {
            currentSize = Mth.lerp(progress / 0.2F, 0.1F, 1.2F);
        } else if (progress < 0.8F) {
            currentSize = 1.2F;
        } else {
            currentSize = Mth.lerp((progress - 0.8F) / 0.2F, 1.2F, 0.1F);
        }
        this.quadSize = currentSize;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new AirdropSmokeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }
}
