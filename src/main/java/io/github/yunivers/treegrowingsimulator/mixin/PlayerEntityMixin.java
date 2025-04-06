package io.github.yunivers.treegrowingsimulator.mixin;

import io.github.yunivers.treegrowingsimulator.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.StationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    @Unique
    boolean oldSneaking;
    @Unique
    double oldX;
    @Unique
    double oldZ;
    @Unique
    int movementCounter = 0;
    @Unique
    int ticksSinceLastCheck;

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void checkForTwerk(CallbackInfo ci)
    {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (ticksSinceLastCheck >= 5)
        {
            ticksSinceLastCheck = 0;

            if (Math.abs(player.x - oldX) > 0.25 || Math.abs(player.z - oldZ) > 0.25)
                movementCounter++;
            if (player.isSneaking() != oldSneaking)
                movementCounter++;

            if (movementCounter > Config.config.waitTime)
            {
                List<BlockPos> blockPositions = getNearestBlocks(player.world, (int)player.x, (int)player.y, (int)player.z);
                for (BlockPos pos : blockPositions)
                {
                    Block block = Block.BLOCKS[player.world.getBlockId(pos.x, pos.y, pos.z)];
                    if ((double) player.world.random.nextFloat() < Config.config.growthChance / 100d)
                        block.onBonemealUse(player.world, pos.x, pos.y, pos.z, player.world.getBlockState(pos.x, pos.y, pos.z));

                    // https://i.imgur.com/oHsdIrr.png
                }
                movementCounter = 0;
            }
        }
        else
            ticksSinceLastCheck++;

        oldSneaking = player.isSneaking();
        updatePlayerPos(player);
    }

    @Unique
    private List<BlockPos> getNearestBlocks(World world, int xpos, int ypos, int zpos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int x = -5; x <= 5; x++)
            for (int y = -2; y <= 2; y++)
                for (int z = -5; z <= 5; z++)
                {
                    int id = world.getBlockId(x + xpos, y + ypos, z + zpos);
                    if (id != 0 && (!(Block.BLOCKS[id] instanceof CropBlock) || !Config.config.ignoreCrops))
                        list.add(new BlockPos(x + xpos, y + ypos, z + zpos));
                }
        return list;
    }

    @Unique
    private void updatePlayerPos(PlayerEntity player)
    {
        oldX = player.x;
        oldZ = player.z;
    }
}
