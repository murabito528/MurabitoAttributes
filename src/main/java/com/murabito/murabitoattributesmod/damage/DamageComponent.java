package com.murabito.murabitoattributesmod.damage;

import java.util.EnumSet;



public class DamageComponent implements Cloneable {
    public double base;
    public ModDamageType finalType;
    public EnumSet<ModDamageType> affectedTypes;

    public double convertedDamageTotal=0;
    public double increasedTotal=0;
    public double criticalMultiTotal=0;

    public DamageComponent(double base, ModDamageType finalType) {
        this.base = base;
        this.finalType = finalType;
        affectedTypes = EnumSet.of(finalType);
    }

    @Override
    public DamageComponent clone() {
        try {
            DamageComponent clone = (DamageComponent) super.clone();
            // EnumSet は mutable なのでコピーを作る
            clone.affectedTypes = EnumSet.copyOf(this.affectedTypes);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
