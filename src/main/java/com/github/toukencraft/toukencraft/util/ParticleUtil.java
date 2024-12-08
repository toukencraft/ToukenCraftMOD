package com.github.toukencraft.toukencraft.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.LevelAccessor;


/** パーティクルの補助関数 */
public class ParticleUtil {
    /**
     *
     * @param world ワールド
     * @param options 発生させるパーティクル
     * @param num パーティクルの個数
     * @param x 中心のx座標
     * @param y 中心のy座標
     * @param z 中心のz座標
     * @param w x方向の範囲
     * @param h y方向の範囲
     * @param d z方向の範囲
     * @param vx x方向の速さ
     * @param vy y方向の速さ
     * @param vz z方向の速さ
     */
    public static void addParticle(
            LevelAccessor world,
            ParticleOptions options,
            int num,
            double x,
            double y,
            double z,
            double w,
            double h,
            double d,
            double vx,
            double vy,
            double vz
    ) {
        if (!world.isClientSide()) {
            return;
        }

        var rnd = world.getRandom();
        for (int i = 0; i < num; i++) {
            world.addParticle(
                    options,
                    x + w * (rnd.nextFloat() - 0.5),
                    y + h * (rnd.nextFloat() - 0.5),
                    z + d * (rnd.nextFloat() - 0.5),
                    vx,
                    vy,
                    vz
            );
        }
    }
}
