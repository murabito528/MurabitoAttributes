package com.murabito.murabitoattributesmod.events;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damagesource.CustomDamageSource;
import com.murabito.murabitoattributesmod.gamerule.CustomGameRules;
import com.murabito.murabitoattributesmod.util.DamageHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID)
public class DamageEvent {

    private static final int BASE_FIRE_TICKS = 60;
    private static final int FIRE_TICKS_PER_DAMAGE = 10;
    private static final int PARTICLE_COUNT = 10;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        Entity attackerEntity = source.getEntity();

        //ダメージログ
        ServerLevel level = (ServerLevel) event.getEntity().level();
        if (level.getGameRules().getBoolean(CustomGameRules.SHOW_DAMAGE_LOG)) {
            String msg = String.format(
                    "[DamageLog] %s took %.2f damage from %s Type:%s",
                    event.getEntity().getName().getString(),
                    event.getAmount(),
                    event.getSource().getMsgId(),
                    source.type().msgId()
            );
            level.getServer().sendSystemMessage(Component.literal(msg));


        // チャット表示用の文字列組み立て
        StringBuilder sb = new StringBuilder();
        sb.append(msg);

        // チャット送信（サーバプレイヤー限定で通知）
        if (target.level().isClientSide) {
            // クライアント側は通常ここに来ないが念のため
            return;
        }
        target.level().players().forEach(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal(sb.toString()));
            }
        });
        }

        // 飛び道具ならシューターを取得
        if (attackerEntity instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity owner) {
            attackerEntity = owner;
        }

        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        // プレイヤー・Mobの近接 or 飛び道具攻撃のみ対象
        if (!(source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.ARROW))) return;

        // 元の物理ダメージ
        float originalDamage = event.getAmount();

        // 攻撃者属性
        double convFire = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_FIRE_DAMAGE_CONV.get());
        double flatFire = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_DAMAGE_BASE.get());
        double convExtraFire = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_FIRE_DAMAGE_EXTRA.get());

        double convIce = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_ICE_DAMAGE_CONV.get());
        double flatIce = getAttributeValueOrZero(attacker, CustomAttributes.ICE_DAMAGE_BASE.get());
        double convExtraIce = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_ICE_DAMAGE_EXTRA.get());

        double convLightning = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_CONV.get());
        double flatLightning = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_DAMAGE_BASE.get());
        double convExtraLightning = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_EXTRA.get());

        // 合計変換率を計算
        double totalConv = convFire + convIce + convLightning;

        // スケーリング係数（100%を超える場合は縮小）
        double scale = totalConv > 1.0 ? 1.0 / totalConv : 1.0;

        // 物理 → 属性変換
        float reducedPhysical = (float) (originalDamage * (1.0 - Math.min(1.0, totalConv)));
        float convertedFireDamage = (float) (originalDamage * convFire * scale);
        float convertedIceDamage = (float) (originalDamage * convIce * scale);
        float convertedLightningDamage = (float) (originalDamage * convLightning * scale);

        // 追加属性ダメージ
        float extraFireDamage = (float) (convertedFireDamage + flatFire + originalDamage*convExtraFire);
        float extraIceDamage = (float) (convertedIceDamage + flatIce + originalDamage*convExtraIce);
        float extraLightningDamage = (float) (convertedLightningDamage + flatLightning + originalDamage*convExtraLightning);

        // 物理ダメージ更新
        event.setAmount(reducedPhysical);

        // 炎ダメージ適用
        if (extraFireDamage > 0) {
            applyFireDamage(target, extraFireDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.FLAME);
        }
        // 氷ダメージ適用
        if (extraIceDamage > 0) {
            applyIceDamage(target, extraIceDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.CLOUD);
        }
        // 雷ダメージ適用
        if (extraLightningDamage > 0) {
            applyLightningDamage(target, extraLightningDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.SNEEZE);
        }
        /*
        // チャット表示用の文字列組み立て
        StringBuilder sb = new StringBuilder();
        sb.append("Original Damage: ").append(originalDamage);
        sb.append(", Reduced Physical Damage: ").append(reducedPhysical);
        sb.append(", Scaling: ").append(scale);
        sb.append(", Convert Fire: ").append(convFire);
        sb.append(", Converted Fire: ").append(convertedFireDamage);
        sb.append(", Extra Fire Damage: ").append(extraFireDamage);

        // チャット送信（サーバプレイヤー限定で通知）
        if (target.level().isClientSide) {
            // クライアント側は通常ここに来ないが念のため
            return;
        }
        target.level().players().forEach(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal(sb.toString()));
            }
        });*/
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        Entity attackerEntity = source.getEntity();

        // 元のダメージ
        float originalDamage = event.getAmount();


        //物理ダメージ反射
        if(source.is(DamageTypes.MOB_ATTACK)||source.is(DamageTypes.PLAYER_ATTACK)){
            double ref = getAttributeValueOrZero(target, CustomAttributes.PHYS_REFLECTION.get());
            if(ref>0) {
                if (attackerEntity instanceof LivingEntity attacker) {
                    attacker.hurt(attacker.damageSources().thorns(target),originalDamage);
                }
            }
        }

        //炎ダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_FIRE)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.FIRE_RESISTANCE.get());
            double ref = getAttributeValueOrZero(target, CustomAttributes.FIRE_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);


            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_RESISTANCE.get());
                    DamageHelper.dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
        }

        //氷ダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_ICE)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.ICE_RESISTANCE.get());
            double ref = getAttributeValueOrZero(target, CustomAttributes.ICE_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);

            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.ICE_RESISTANCE.get());
                    DamageHelper.dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
        }

        //雷ダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_LIGHTNING)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.LIGHTNING_RESISTANCE.get());
            double ref = getAttributeValueOrZero(target, CustomAttributes.LIGHTNING_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);

            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_RESISTANCE.get());
                    DamageHelper.dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
        }
    }

    /** 属性値を安全に取得（nullなら0） */
    private static double getAttributeValueOrZero(LivingEntity entity, net.minecraft.world.entity.ai.attributes.Attribute attr) {
        AttributeInstance inst = entity.getAttribute(attr);
        return inst != null ? inst.getValue() : 0.0;
    }

    /** 炎ダメージと燃焼時間の付与（無敵時間を回避） */
    private static void applyFireDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        target.setRemainingFireTicks(BASE_FIRE_TICKS + (int) (damage * FIRE_TICKS_PER_DAMAGE));
        DamageHelper.dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_FIRE,damage);
    }

    /** 氷ダメージと移動速度低下の付与（無敵時間を回避） */
    private static void applyIceDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        //target.setRemainingFireTicks(BASE_FIRE_TICKS + (int) (damage * FIRE_TICKS_PER_DAMAGE));
        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                60 + (int)damage,  // tick数（20tick = 1秒）
                (int) Math.min(3, Math.max(damage / 10 - 1,0))    // 効果レベル（0 = Lv1, 1 = Lv2...）
        ));
        DamageHelper.dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_ICE,damage);
    }

    /** 雷ダメージと(未定)の付与（無敵時間を回避） */
    private static void applyLightningDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        DamageHelper.dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_LIGHTNING,damage);
    }

    /** パーティクルを発生させる */
    private static void spawnParticles(ServerLevel level, LivingEntity target, ParticleOptions type) {
        level.sendParticles(
                type, // ← 渡された種類を使う
                target.getX(),
                target.getY() + target.getBbHeight() / 2,
                target.getZ(),
                PARTICLE_COUNT,
                0.3, 0.5, 0.3,
                0.07
        );
    }
}