package inc.zorold.ret.potion.recipe;

import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class LongReturnPotionRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        return input.getItem() == ReturnPotionRegistry.RETURN_POTION.get();
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == Items.REDSTONE;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ReturnPotionRegistry.LONG_RETURN_POTION.get());

        return result;
    }
}
