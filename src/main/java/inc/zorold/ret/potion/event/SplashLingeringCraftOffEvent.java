package inc.zorold.ret.potion.event;

import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "return_potion_zoro", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SplashLingeringCraftOffEvent {
    @SubscribeEvent
    public static void craftOff(PotionBrewEvent.Pre event){
        ItemStack ingredient = event.getItem(3);

        //Проверяем, что ингредиент не порох или дыхание дракона
        if (ingredient.getItem() != Items.GUNPOWDER && ingredient.getItem() != Items.DRAGON_BREATH) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            ItemStack input = event.getItem(i);
            // Если находим хоть одну нашу бутылку
            if (PotionUtils.getPotion(input) == ReturnPotionRegistry.RETURN_POTION.get() ||
                    PotionUtils.getPotion(input) == ReturnPotionRegistry.LONG_RETURN_POTION.get()) {
                event.setCanceled(true);
                return; // Событие уже отменено, дальше проверять нет смысла
            }
        }
    }
}
