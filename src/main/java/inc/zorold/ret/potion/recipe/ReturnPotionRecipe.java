package inc.zorold.ret.potion.recipe;

import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class ReturnPotionRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack itemStack) {
        if (itemStack.getItem() == Items.POTION) {
            return PotionUtils.getPotion(itemStack) == Potions.AWKWARD;
        }
        return false;
    }

    @Override
    public boolean isIngredient(ItemStack itemStack) {
        return itemStack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ReturnPotionRegistry.RETURN_POTION.get());

        return result;
    }
}
