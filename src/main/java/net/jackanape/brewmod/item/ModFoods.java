package net.jackanape.brewmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class ModFoods {

    public static final FoodProperties LAGER_PINT_PROPERTIES =
            (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F)
                    .effect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 0), 0.1F)
                    .alwaysEat()
                    .build();


}
