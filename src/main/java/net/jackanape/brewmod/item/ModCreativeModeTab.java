package net.jackanape.brewmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab BREW_TAB = new CreativeModeTab("brewtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.LAGER_PINT.get());
        }
    };
}
