package com.murabito.murabitoattributesmod.damage;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.damage.stages.*;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class DamagePipeLine {
    private final List<DamageStage> stages = new ArrayList<>();

    private static final DamagePipeLine INSTANCE = new DamagePipeLine();

    public DamagePipeLine() {
        // 処理の順序を定義
        //命中、クリティカル判定
        stages.add(new AccuracyStage());//命中判定
        stages.add(new BlockStage());//ブロック判定
        stages.add(new CritStage());//クリティカル判定


        stages.add(new AddDamageStage());//A~Bの追加ダメージを与える
        stages.add(new ConvDamageStage());//AダメージのB%をCダメージに変換する&AダメージのB%をCダメージとして追加する
        //stages.add(new GainDamageStage());//AダメージのB%をCダメージとして追加する


        stages.add(new ModifierStage());//inc/more/クリティカル倍率
        stages.add(new ConversionStage());//taken as
        stages.add(new DefenseStage());//防御力、耐性による軽減
        stages.add(new AilmentStage());// onHitイベントとか発火とか

        stages.add(new ApplyStage());//ダメージの適用
    }

    public void process(HitData hitData) {
        if (!(hitData.target.level() instanceof ServerLevel)) return;
        for (DamageStage stage : stages) {
            if (!stage.apply(hitData)) break; // falseで中断（例: Miss）
        }
    }

    public static DamagePipeLine get() {
        return INSTANCE;
    }
}
