package com.murabito.murabitoattributesmod.events;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damagesource.CustomDamageSource;
import com.murabito.murabitoattributesmod.damagesource.ModDamageTypeTags;
import com.murabito.murabitoattributesmod.gamerule.CustomGameRules;
import com.murabito.murabitoattributesmod.util.DamageData;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import static com.murabito.murabitoattributesmod.util.DamageHelper.dealDamageWithType;
import static com.murabito.murabitoattributesmod.util.Util.getAttributeValueOrZero;

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

        // 飛び道具ならシューターを取得
        if (attackerEntity instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity owner) {
            attackerEntity = owner;
        }

        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        // プレイヤー・Mobの近接 or 飛び道具攻撃のみ対象
        //if (!(source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.ARROW))) return;
        //こっちにするとlog4エラー
        if(!source.is(ModDamageTypeTags.IS_TRACKING)) return;

        DamageData damageData = new DamageData(event.getAmount(),source,attacker,target);

        damageData.calc_damage();

        // 物理ダメージ更新
        event.setAmount(damageData.reducedPhysical);

        // 炎ダメージ適用
        if (damageData.finalFireDamage > 0) {
            applyFireDamage(target, damageData.finalFireDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.FLAME);
        }
        // 氷ダメージ適用
        if (damageData.finalIceDamage > 0) {
            applyIceDamage(target, damageData.finalIceDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.CLOUD);
        }
        // 雷ダメージ適用
        if (damageData.finalLightningDamage > 0) {
            applyLightningDamage(target, damageData.finalLightningDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.SNEEZE);
        }
        // カオスダメージ適用
        if (damageData.finalChaosDamage > 0) {
            applyChaosDamage(target, damageData.finalChaosDamage, attacker);
            spawnParticles((ServerLevel) target.level(), target, ParticleTypes.WITCH);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        Entity attackerEntity = source.getEntity();

        // 元のダメージ
        float originalDamage = event.getAmount();

        //ログ用
        float finalDmg=originalDamage;
        double resist=0;

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
            double resi_max = getAttributeValueOrZero(target, CustomAttributes.FIRE_RESISTANCE_MAX.get());
            double pene = (attackerEntity instanceof LivingEntity)
                    ? getAttributeValueOrZero((LivingEntity) attackerEntity, CustomAttributes.FIRE_PENETRATION.get())
                    : 0.0;
            resi = Math.min(resi,resi_max) - pene;
            double ref = getAttributeValueOrZero(target, CustomAttributes.FIRE_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);


            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_RESISTANCE.get());
                    dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
            resist = resi;
            finalDmg = reducedDamage;
        }

        //氷ダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_ICE)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.ICE_RESISTANCE.get());
            double resi_max = getAttributeValueOrZero(target, CustomAttributes.ICE_RESISTANCE_MAX.get());
            double pene = (attackerEntity instanceof LivingEntity)
                    ? getAttributeValueOrZero((LivingEntity) attackerEntity, CustomAttributes.ICE_PENETRATION.get())
                    : 0.0;
            resi = Math.min(resi,resi_max) - pene;
            double ref = getAttributeValueOrZero(target, CustomAttributes.ICE_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);

            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.ICE_RESISTANCE.get());
                    dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
            resist = resi;
            finalDmg = reducedDamage;
        }

        //雷ダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_LIGHTNING)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.LIGHTNING_RESISTANCE.get());
            double resi_max = getAttributeValueOrZero(target, CustomAttributes.LIGHTNING_RESISTANCE_MAX.get());
            double pene = (attackerEntity instanceof LivingEntity)
                    ? getAttributeValueOrZero((LivingEntity) attackerEntity, CustomAttributes.LIGHTNING_PENETRATION.get())
                    : 0.0;
            resi = Math.min(resi,resi_max) - pene;
            double ref = getAttributeValueOrZero(target, CustomAttributes.LIGHTNING_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);

            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_RESISTANCE.get());
                    dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
            resist = resi;
            finalDmg = reducedDamage;
        }

        //カオスダメージ軽減
        if(source.is(CustomDamageSource.ELEMENTAL_CHAOS)){
            double resi = getAttributeValueOrZero(target, CustomAttributes.CHAOS_RESISTANCE.get());
            double resi_max = getAttributeValueOrZero(target, CustomAttributes.CHAOS_RESISTANCE_MAX.get());
            double pene = (attackerEntity instanceof LivingEntity)
                    ? getAttributeValueOrZero((LivingEntity) attackerEntity, CustomAttributes.CHAOS_PENETRATION.get())
                    : 0.0;
            resi = Math.min(resi,resi_max) - pene;
            double ref = getAttributeValueOrZero(target, CustomAttributes.CHAOS_REFLECTION.get());
            float reducedDamage = (float) (originalDamage * (1-resi));
            event.setAmount(reducedDamage);

            if(ref>0){
                if(attackerEntity instanceof LivingEntity attacker) {
                    double attackerResi = getAttributeValueOrZero(attacker, CustomAttributes.CHAOS_RESISTANCE.get());
                    dealDamageWithType(attacker,target,(ServerLevel)target.level(),CustomDamageSource.REFLECT,
                            (float)(reducedDamage * ref * (1-attackerResi)));
                }
            }
            resist = resi;
            finalDmg = reducedDamage;
        }

        //ダメージログ
        ServerLevel level = (ServerLevel) event.getEntity().level();
        if (level.getGameRules().getBoolean(CustomGameRules.SHOW_DAMAGE_LOG)) {
            String sourceEntityName = (attackerEntity != null) ? attackerEntity.getName().getString() : "null";

            String msg = String.format(
                    "[DamageLog] %s -> %s %.1f dmg (%.1f%% resisted, %.1f final, Type:%s)",
                    sourceEntityName,
                    event.getEntity().getName().getString(),
                    originalDamage,
                    resist*100,
                    finalDmg,
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
    }



    /** 炎ダメージと燃焼時間の付与（無敵時間を回避） */
    private static void applyFireDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        target.setRemainingFireTicks(BASE_FIRE_TICKS + (int) (damage * FIRE_TICKS_PER_DAMAGE));
        dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_FIRE,damage);
    }

    /** 氷ダメージと移動速度低下の付与（無敵時間を回避） */
    private static void applyIceDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        //target.setRemainingFireTicks(BASE_FIRE_TICKS + (int) (damage * FIRE_TICKS_PER_DAMAGE));
        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                60 + (int)damage,  // tick数（20tick = 1秒）
                (int) Math.min(3, Math.max(damage / 10 - 1,0))    // 効果レベル（0 = Lv1, 1 = Lv2...）
        ));
        dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_ICE,damage);
    }

    /** 雷ダメージと(未定)の付与（無敵時間を回避） */
    private static void applyLightningDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_LIGHTNING,damage);
        MurabitoAttributesMod.LOGGER.info(attacker.toString());
    }

    /** カオスダメージと(未定)の付与（無敵時間を回避） */
    private static void applyChaosDamage(LivingEntity target, float damage, @Nullable LivingEntity attacker) {
        dealDamageWithType(target,attacker,(ServerLevel)target.level(),CustomDamageSource.ELEMENTAL_CHAOS,damage);
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