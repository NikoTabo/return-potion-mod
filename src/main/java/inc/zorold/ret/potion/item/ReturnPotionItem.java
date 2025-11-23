package inc.zorold.ret.potion.item;

import inc.zorold.ret.potion.event.ReturnPotionEvents;
import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ReturnPotionItem extends Item {
    private final int duration;

    public ReturnPotionItem(Properties properties, int duration) {
        super(properties);
        this.duration = duration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            MobEffect myEffect = ReturnPotionRegistry.RETURN_EFFECT.get();
            player.addEffect(new MobEffectInstance(myEffect, duration, 0));
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        int totalSeconds = duration / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);

        tooltip.add(Component.translatable("tooltip.return_potion_zoro.return_potion.duration",
                timeText).withStyle(ChatFormatting.BLUE));
    }
}
