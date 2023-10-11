package com.bigenergy.achiopt.mixin;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryChangedCriterion.class)
public class InventoryChangedCriterionMixin {

    private int ticksSkipped;

    private boolean tryTick()
    {
        int skipTicksAmount = 5;
        if (skipTicksAmount <= 0)
            return true;

        this.ticksSkipped++;
        if (this.ticksSkipped > skipTicksAmount)
        {
            this.ticksSkipped = 0;
            return true;
        }

        return false;
    }

    public void trigger(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack) {

        if (!this.tryTick())
            return;

        int i = 0;
        int j = 0;
        int k = 0;

        for(int l = 0; l < inventory.size(); ++l) {
            ItemStack itemStack = inventory.getStack(l);
            if (itemStack.isEmpty()) {
                ++j;
            } else {
                ++k;
                if (itemStack.getCount() >= itemStack.getMaxCount()) {
                    ++i;
                }
            }
        }

        this.trigger(player, inventory, stack, i, j, k);
    }
    @Shadow
    private void trigger(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied) {

    }

}
