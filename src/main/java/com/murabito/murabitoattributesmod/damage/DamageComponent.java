package com.murabito.murabitoattributesmod.damage;

import java.util.EnumSet;



public class DamageComponent implements Cloneable {
    public double base;
    public double convertedDamageTotal;
    public ModDamageType finalType;
    public EnumSet<ModDamageType> affectedTypes;

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
