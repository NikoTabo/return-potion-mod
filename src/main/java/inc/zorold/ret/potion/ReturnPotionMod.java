package inc.zorold.ret.potion;

import com.mojang.logging.LogUtils;
import inc.zorold.ret.potion.recipe.AdvancedReturnPotionRecipe;
import inc.zorold.ret.potion.recipe.LongReturnPotionRecipe;
import inc.zorold.ret.potion.recipe.ReturnPotionRecipe;
import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod("return_potion_zoro")
public class ReturnPotionMod {
    public static final String MOD_ID = "return_potion_zoro";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ReturnPotionMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ReturnPotionRegistry.EFFECTS.register(modEventBus);
        ReturnPotionRegistry.POTIONS.register(modEventBus);
        ReturnPotionRegistry.ITEMS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new ReturnPotionRecipe());
            BrewingRecipeRegistry.addRecipe(new LongReturnPotionRecipe());
            BrewingRecipeRegistry.addRecipe(new AdvancedReturnPotionRecipe());
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ReturnPotionRegistry.ADVANCED_RETURN_POTION);
        }
    }
}
