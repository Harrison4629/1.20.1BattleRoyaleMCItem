package net.harrison.battleroyaleitem.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ParticleSummon {
    private static final Random RANDOM = new Random();

    /**
     * 在指定位置生成粒子效果（服务端）
     * @param level 世界
     * @param pos 位置
     * @param particle 粒子类型
     * @param count 粒子数量
     */
    public static void spawnParticles(Level level, Vec3 pos, ParticleOptions particle, int count) {
        spawnParticles(level, pos.x, pos.y, pos.z, particle, count, 0.5, 0.5, 0.5, 0.1);
    }

    /**
     * 在指定位置生成粒子效果（服务端，带扩散范围）
     * @param level 世界
     * @param x 位置X
     * @param y 位置Y
     * @param z 位置Z
     * @param particle 粒子类型
     * @param count 粒子数量
     * @param deltaX X方向扩散范围
     * @param deltaY Y方向扩散范围
     * @param deltaZ Z方向扩散范围
     * @param speed 粒子速度
     */
    public static void spawnParticles(Level level, double x, double y, double z,
                                      ParticleOptions particle, int count,
                                      double deltaX, double deltaY, double deltaZ, double speed) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static void spawnClientParticles(Vec3 pos, ParticleOptions particle, int count) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            for (int i = 0; i < count; i++) {
                double offsetX = (RANDOM.nextDouble() - 0.5) * 0.5;
                double offsetY = (RANDOM.nextDouble() - 0.5) * 0.5;
                double offsetZ = (RANDOM.nextDouble() - 0.5) * 0.5;

                mc.level.addParticle(particle,
                        pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
                        0, 0, 0);
            }
        }
    }


    /**
     * 魔法闪烁效果
     */
    public static void magicSparkle(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.ENCHANT, count);
    }

    /**
     * 爆炸效果
     */
    public static void explosion(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.EXPLOSION, count/2);
        spawnParticles(level, pos, ParticleTypes.LARGE_SMOKE, count);
    }

    /**
     * 治愈效果
     */
    public static void healingEffect(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.HEART, count/2);
        spawnParticles(level, pos, ParticleTypes.HAPPY_VILLAGER, count);
    }

    /**
     * 火焰效果
     */
    public static void fireEffect(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.FLAME, count);
        spawnParticles(level, pos, ParticleTypes.SMOKE, count/2);
    }

    /**
     * 水花效果
     */
    public static void splashEffect(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.SPLASH, count);
        spawnParticles(level, pos, ParticleTypes.BUBBLE, count/2);
    }

    /**
     * 传送效果
     */
    public static void teleportEffect(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.PORTAL, count);
        spawnParticles(level, pos, ParticleTypes.REVERSE_PORTAL, count/2);
    }

    /**
     * 破坏效果
     */
    public static void breakEffect(Level level, Vec3 pos, int count) {
        spawnParticles(level, pos, ParticleTypes.CLOUD, count);
        spawnParticles(level, pos, ParticleTypes.POOF, count/2);
    }

    /**
     * 圆形粒子环
     * @param level 世界
     * @param center 中心位置
     * @param radius 半径
     * @param particle 粒子类型
     * @param count 粒子数量
     */
    public static void spawnParticleCircle(Level level, Vec3 center, double radius,
                                           ParticleOptions particle, int count) {
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            spawnParticles(level, x, center.y, z, particle, 1, 0.1, 0.1, 0.1, 0.05);
        }
    }

    /**
     * 螺旋粒子效果
     * @param level 世界
     * @param center 中心位置
     * @param height 螺旋高度
     * @param particle 粒子类型
     */
    public static void spawnParticleSpiral(Level level, Vec3 center, double height,
                                           ParticleOptions particle) {
        int steps = 50;
        for (int i = 0; i < steps; i++) {
            double progress = (double) i / steps;
            double angle = progress * 4 * Math.PI; // 两圈
            double radius = (1 - progress); // 逐渐缩小半径
            double y = center.y + height * progress;

            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);

            spawnParticles(level, x, y, z, particle, 1, 0.05, 0.05, 0.05, 0.02);
        }
    }
}
