package inc.zorold.ret.potion.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ReturnEffect extends MobEffect {
    public ReturnEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
