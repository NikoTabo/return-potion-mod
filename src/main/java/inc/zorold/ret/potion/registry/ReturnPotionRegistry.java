package inc.zorold.ret.potion.registry;

import inc.zorold.ret.potion.ReturnPotionMod;
import inc.zorold.ret.potion.effect.ReturnEffect;
import inc.zorold.ret.potion.item.AdvancedReturnPotionItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ReturnPotionRegistry {
    //Создание регистра зелий для мода MOD_ID
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ReturnPotionMod.MOD_ID);
    //Создание регистра эффектов для мода MOD_ID
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ReturnPotionMod.MOD_ID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ReturnPotionMod.MOD_ID);

    //Регистрация эффекта и зелья
    public static final RegistryObject<MobEffect> RETURN_EFFECT = EFFECTS.register(
            "return_effect",
            () -> new ReturnEffect(MobEffectCategory.NEUTRAL, 0x056b57));

    public static final RegistryObject<Potion> RETURN_POTION = POTIONS.register(
            "return_potion",
            () -> new Potion(new MobEffectInstance(RETURN_EFFECT.get(), 20 * 30)));

    public static final RegistryObject<Potion> LONG_RETURN_POTION = POTIONS.register(
            "long_return_potion",
            () -> new Potion(new MobEffectInstance(RETURN_EFFECT.get(), 20 * 90)));

    public static final RegistryObject<Item> ADVANCED_RETURN_POTION = ITEMS.register(
            "advanced_return_potion",
            () -> new AdvancedReturnPotionItem(new Item.Properties().stacksTo(1)));
}
