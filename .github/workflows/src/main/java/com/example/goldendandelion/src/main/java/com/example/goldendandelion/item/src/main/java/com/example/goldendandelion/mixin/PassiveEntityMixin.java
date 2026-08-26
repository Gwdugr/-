package com.example.goldendandelion.mixin;

import com.example.goldendandelion.item.GoldenDandelionItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin extends AnimalEntity {
    
    protected PassiveEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "growUp", at = @At("HEAD"), cancellable = true)
    private void preventGrowingUp(CallbackInfo ci) {
        if (this.getPersistentData().getBoolean(GoldenDandelionItem.GOLDEN_DANDELION_TAG)) {
            ci.cancel();
        }
    }
}
