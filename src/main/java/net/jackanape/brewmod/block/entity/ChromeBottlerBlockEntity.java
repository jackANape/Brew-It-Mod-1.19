package net.jackanape.brewmod.block.entity;

import net.jackanape.brewmod.item.ModItems;
import net.jackanape.brewmod.screen.ChromeBottlerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;

@SuppressWarnings("ALL")
public class ChromeBottlerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 24;

    public ChromeBottlerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHROME_BOTTLER.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ChromeBottlerBlockEntity.this.progress;
                    case 1 -> ChromeBottlerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ChromeBottlerBlockEntity.this.progress = value;
                    case 1 -> ChromeBottlerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Chrome Bottler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ChromeBottlerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("chrome_bottler.progress", this.progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("chrome_bottler.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ChromeBottlerBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (hasRecipe(pEntity) && //check whether slots 1 and 2 are at max cap, if they are dont process
                pEntity.itemHandler.getStackInSlot(1).getCount() < pEntity.itemHandler.getStackInSlot(1).getMaxStackSize() &&
                pEntity.itemHandler.getStackInSlot(2).getCount() < pEntity.itemHandler.getStackInSlot(2).getMaxStackSize()) {

            pEntity.progress++;
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static int beerCubeCapacity = 24; //equals 24 pints
    private static boolean isBucket;

    private static void craftItem(ChromeBottlerBlockEntity pEntity) {

        if (hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(0, 1, false);

            if(isBucket)
            {
                pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.BEER_WATER_BUCKET.get(),
                        pEntity.itemHandler.getStackInSlot(2).getCount() + 1));

                beerCubeCapacity = 0;
            }
            else
            {
                pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.LAGER_PINT.get(),
                        pEntity.itemHandler.getStackInSlot(2).getCount() + 1));

                beerCubeCapacity--;
            }



            pEntity.resetProgress();
        }

        if(beerCubeCapacity == 0)
        {
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(1, new ItemStack(ModItems.BEER_CUBE_EMPTY.get(),
                    pEntity.itemHandler.getStackInSlot(1).getCount() + 1));

            beerCubeCapacity = 24;
        }
    }

    //change items in slots from here
    private static boolean hasRecipe(ChromeBottlerBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean emptyPintReady = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.EMPTY_PINT_GLASS.get();
        boolean beerCubeReady = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.BEER_CUBE.get();

        boolean bucketReady = false;
        isBucket = false;
        if(entity.itemHandler.getStackInSlot(0).getItem() == Items.BUCKET)
        {
            bucketReady = true;
            isBucket= true;
        }

        return (emptyPintReady || bucketReady) && beerCubeReady;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }
}
