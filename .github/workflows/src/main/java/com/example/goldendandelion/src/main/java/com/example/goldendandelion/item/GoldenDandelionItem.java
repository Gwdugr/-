package com.example.goldendandelion.item;

import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class GoldenDandelionItem extends Item {
    
    public static final String GOLDEN_DANDELION_TAG = "GoldenDandelionMarked";
    
    public GoldenDandelionItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, net.minecraft.entity.LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        
        if (entity instanceof PassiveEntity passiveEntity && passiveEntity.isBaby()) {
            if (!world.isClient) {
                markEntity(passiveEntity);
                
                world.playSound(null, entity.getBlockPos(), 
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 
                    entity.getSoundCategory(), 1.0F, 1.0F);
                
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.GLOW,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        20, 0.5, 0.5, 0.5, 0.1);
                }
                
                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                
                user.sendMessage(Text.translatable("message.goldendandelion.success"), true);
            }
            return ActionResult.success(world.isClient);
        }
        
        if (!world.isClient) {
            user.sendMessage(Text.translatable("message.goldendandelion.failure"), true);
        }
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        
        if (!world.isClient && player != null) {
            Box searchBox = new Box(context.getBlockPos()).expand(5.0);
            List<PassiveEntity> babies = world.getEntitiesByClass(
                PassiveEntity.class, 
                searchBox, 
                entity -> entity.isBaby()
            );
            
            if (!babies.isEmpty()) {
                int markedCount = 0;
                for (PassiveEntity baby : babies) {
                    markEntity(baby);
                    markedCount++;
                }
                
                world.playSound(null, context.getBlockPos(), 
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 
                    player.getSoundCategory(), 1.0F, 1.0F);
                
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.GLOW,
                        context.getBlockPos().getX() + 0.5,
                        context.getBlockPos().getY() + 1,
                        context.getBlockPos().getZ() + 0.5,
                        markedCount * 10, 2, 1, 2, 0.1);
                }
                
                if (!player.getAbilities().creativeMode) {
                    context.getStack().decrement(1);
                }
                
                player.sendMessage(Text.translatable("message.goldendandelion.area_success", markedCount), true);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private void markEntity(PassiveEntity entity) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putBoolean(GOLDEN_DANDELION_TAG, true);
    }
}
