package inc.zorold.ret.potion.item;

import inc.zorold.ret.potion.event.ReturnPotionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class AdvancedReturnPotionItem extends Item {
    public AdvancedReturnPotionItem(Properties properties) {
        super(properties);


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
        super.finishUsingItem(stack, level, entity);

        if (entity instanceof ServerPlayer player) {
            CompoundTag itemTag = stack.getOrCreateTag();

            if (itemTag.getBoolean(ReturnPotionEvents.TAG_ACTIVE)) {
                if (player.getUUID().equals(itemTag.getUUID("OwnerUUID"))) {
                    ReturnPotionEvents.returnPlayerBack(player, itemTag);

                    return new ItemStack(Items.GLASS_BOTTLE);
                } else {
                    String ownerName = "Unknown"; // Текст по умолчанию

                    if (itemTag.contains("OwnerName")) {
                        ownerName = itemTag.getString("OwnerName");
                    }

                    Component message = Component.translatable(
                            "message.return_potion_zoro.wrong_owner",
                            ownerName
                    ).withStyle(ChatFormatting.RED);

                    player.displayClientMessage(message, true);
                }
            } else {
                itemTag.putDouble(ReturnPotionEvents.TAG_X, player.getX());
                itemTag.putDouble(ReturnPotionEvents.TAG_Y, player.getY());
                itemTag.putDouble(ReturnPotionEvents.TAG_Z, player.getZ());
                itemTag.putString(ReturnPotionEvents.TAG_DIM, player.level().dimension().location().toString());
                itemTag.putBoolean(ReturnPotionEvents.TAG_ACTIVE, true);

                itemTag.putUUID("OwnerUUID", player.getUUID());
                itemTag.putString("OwnerName", player.getName().getString());
                ReturnPotionEvents.teleportToSpawn(player);

                return stack;
            }
        }

        return stack;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(ReturnPotionEvents.TAG_ACTIVE);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().getBoolean(ReturnPotionEvents.TAG_ACTIVE)) {
            tooltip.add(Component.translatable("tooltip.return_potion_zoro.advanced_potion.active")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.return_potion_zoro.advanced_potion.inactive")
                    .withStyle(ChatFormatting.GRAY)); // Серый текст
        }
    }
}
