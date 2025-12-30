package com.murabito.murabitoattributesmod.damage;

public interface DamageStage {
    /**
     * @return trueなら次のステージへ進む。falseなら中断。
     */
    boolean apply(HitData hitData);
}