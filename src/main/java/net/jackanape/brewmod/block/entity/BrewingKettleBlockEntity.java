package net.jackanape.brewmod.block.entity;

import net.jackanape.brewmod.item.ModItems;
import net.jackanape.brewmod.screen.BrewingKettleMenu;
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

@SuppressWarnings("ALL")
public class BrewingKettleBlockEntity extends BlockEntity implements MenuProvider {

    public static int slotAmount = 4;

    private final ItemStackHandler itemHandler = new ItemStackHandler(slotAmount) { //change here for slot amount in gui
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200; //20 ticks per second, progress is in seconds,

    public BrewingKettleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREWING_KETTLE.get(), pos, state);

        //try adding time data through this
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BrewingKettleBlockEntity.this.progress;
                    case 1 -> BrewingKettleBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrewingKettleBlockEntity.this.progress = value;
                    case 1 -> BrewingKettleBlockEntity.this.maxProgress = value;
                }
            }



            @Override
            public int getCount() {
                return slotAmount;
            }
        };

    }


    @Override
    public Component getDisplayName() {
        return Component.literal("BrewingKettle");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BrewingKettleMenu(id, inventory, this, this.data);
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
        nbt.putInt("brewing_kettle.progress", this.progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("brewing_kettle.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, BrewingKettleBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        //20 ticks a second. maxprogress is the time length: 1200 is 1 minute
        if (hasRecipe(pEntity)) {
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

    private static void craftItem(BrewingKettleBlockEntity pEntity) {

        if (hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(0, 1, false);
            pEntity.itemHandler.setStackInSlot(0, new ItemStack(ModItems.WORT.get()));

            pEntity.itemHandler.extractItem(1, 1, false); //remove boiling water
            pEntity.itemHandler.setStackInSlot(1, new ItemStack(Items.BUCKET));

            pEntity.itemHandler.extractItem(2, 1, false);
            pEntity.itemHandler.extractItem(3, 21, false);


            pEntity.resetProgress();
        }
    }

    //change items in slots from here
    private static boolean hasRecipe(BrewingKettleBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean emptyCubeReady = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.BEER_CUBE_EMPTY.get();
        boolean boilingWaterReady = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.BOILING_WATER_BUCKET.get();
        boolean mashExtractReady = entity.itemHandler.getStackInSlot(2).getItem() == ModItems.MASH_EXTRACT.get();

        boolean hopsReady = false;
        if(entity.itemHandler.getStackInSlot(3).getItem() == ModItems.HOPS.get() &&
                entity.itemHandler.getStackInSlot(3).getCount() >= 21)
        {
            hopsReady = true;
        }


        return emptyCubeReady && boilingWaterReady && mashExtractReady && hopsReady;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(1).getItem() == stack.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
