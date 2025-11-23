package inc.zorold.ret.potion.recipe;

import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class AdvancedReturnPotionRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        if(input.getItem() == Items.POTION){
            return PotionUtils.getPotion(input) == ReturnPotionRegistry.RETURN_POTION.get();
        }
        return false;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == Items.CHORUS_FRUIT;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!isInput(input) || !isIngredient(ingredient)){
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ReturnPotionRegistry.ADVANCED_RETURN_POTION.get());

        return result;
    }
}
