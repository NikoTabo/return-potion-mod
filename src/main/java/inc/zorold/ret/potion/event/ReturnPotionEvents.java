package inc.zorold.ret.potion.event;

import inc.zorold.ret.potion.registry.ReturnPotionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = "return_potion_zoro", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReturnPotionEvents {
    public static final String TAG_X = "ret_pot_x";
    public static final String TAG_Y = "ret_pot_y";
    public static final String TAG_Z = "ret_pot_z";
    public static final String TAG_DIM = "ret_pot_dim";
    public static final String TAG_ACTIVE = "ret_pot_active";

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();

        // Проверяем, что это наше зелье и это сервер, и это игрок
        if (instance.getEffect() == ReturnPotionRegistry.RETURN_EFFECT.get()
                && entity instanceof ServerPlayer player
                && !player.level().isClientSide()) {

            CompoundTag data = player.getPersistentData();

            // Сохраняем текущую позицию (откуда уходим)
            data.putDouble(TAG_X, player.getX());
            data.putDouble(TAG_Y, player.getY());
            data.putDouble(TAG_Z, player.getZ());
            data.putString(TAG_DIM, player.level().dimension().location().toString());
            data.putBoolean(TAG_ACTIVE, true);

            // Телепортируем на спавн/кровать
            teleportToSpawn(player);
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();

        if (instance != null && instance.getEffect() == ReturnPotionRegistry.RETURN_EFFECT.get()
                && entity instanceof ServerPlayer player
                && !player.level().isClientSide()) {
            returnPlayerBack(player, player.getPersistentData());
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();

        if (instance != null && instance.getEffect() == ReturnPotionRegistry.RETURN_EFFECT.get()
                && entity instanceof ServerPlayer player
                && !player.level().isClientSide()) {

            returnPlayerBack(player, player.getPersistentData());
            clearReturnData(player);
        }
    }

    public static void returnPlayerBack(ServerPlayer player, CompoundTag data) {
        if (data.getBoolean(TAG_ACTIVE)) {
            double x = data.getDouble(TAG_X);
            double y = data.getDouble(TAG_Y);
            double z = data.getDouble(TAG_Z);
            String dimStr = data.getString(TAG_DIM);

            clearReturnData(player);

            MinecraftServer server = player.getServer();
            if (server != null) {
                for (ServerLevel destinationLevel : server.getAllLevels()) {
                    if (destinationLevel.dimension().location().toString().equals(dimStr)) {

                        if (player.level() instanceof ServerLevel currentLevel) {
                            currentLevel.sendParticles(ParticleTypes.PORTAL,
                                    player.getX(), player.getY() + 1.0, player.getZ(),
                                    100, 0.5, 1.0, 0.5, 0.1);

                            currentLevel.playSound(null,
                                    player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                        }

                        player.teleportTo(destinationLevel, x, y, z, player.getYRot(), player.getXRot());

                        destinationLevel.sendParticles(ParticleTypes.PORTAL,
                                x, y + 1.0, z,
                                100, 0.5, 1.0, 0.5, 0.1);

                        destinationLevel.playSound(null,
                                x, y, z,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

                        return;
                    }
                }
            }
        }
    }

    private static void clearReturnData(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove(TAG_X);
        data.remove(TAG_Y);
        data.remove(TAG_Z);
        data.remove(TAG_DIM);
        data.remove(TAG_ACTIVE);
    }

    public static void teleportToSpawn(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerLevel spawnLevel = server.getLevel(player.getRespawnDimension());
        if (spawnLevel == null) spawnLevel = server.overworld();

        BlockPos respawnPos = player.getRespawnPosition();
        Vec3 targetPos = null;

        if (respawnPos != null) {
            BlockState state = spawnLevel.getBlockState(respawnPos);
            Direction direction = null;
            if (state.getBlock() instanceof BedBlock) {
                direction = state.getValue(BedBlock.FACING);
            }
            if (direction != null) {
                Optional<Vec3> bedPos = BedBlock.findStandUpPosition(
                        EntityType.PLAYER,
                        spawnLevel,
                        respawnPos,
                        direction,
                        player.getRespawnAngle()
                );
                if (bedPos.isPresent()) {
                    targetPos = bedPos.get();
                }
            }
        }

        if (targetPos == null) {
            spawnLevel = server.overworld(); // Если кровати нет - всегда оверворлд
            BlockPos worldSpawn = spawnLevel.getSharedSpawnPos();
            targetPos = new Vec3(worldSpawn.getX() + 0.5, worldSpawn.getY() + 1, worldSpawn.getZ() + 0.5);
        }

        if (player.level() instanceof ServerLevel currentLevel) {
            currentLevel.sendParticles(ParticleTypes.PORTAL,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    100, 0.5, 1.0, 0.5, 0.1);

            currentLevel.playSound(null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        player.teleportTo(spawnLevel, targetPos.x, targetPos.y, targetPos.z, player.getRespawnAngle(), 0f);

        spawnLevel.sendParticles(ParticleTypes.PORTAL,
                targetPos.x, targetPos.y + 1.0, targetPos.z,
                100, 0.5, 1.0, 0.5, 0.1);

        spawnLevel.playSound(null,
                targetPos.x, targetPos.y, targetPos.z,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
