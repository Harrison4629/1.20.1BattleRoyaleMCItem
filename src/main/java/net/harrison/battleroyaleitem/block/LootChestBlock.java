package net.harrison.battleroyaleitem.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.harrison.battleroyaleitem.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class LootChestBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(0.5, 0, 3.3, 15.5, 7.3, 13.4),
            Direction.SOUTH, Block.box(0.5, 0, 2.6, 15.5, 7.3, 12.7),
            Direction.WEST,  Block.box(3.3, 0, 0.5, 13.4, 7.3, 15.5),
            Direction.EAST,  Block.box(2.6, 0, 0.5, 12.7, 7.3, 15.5)
    ));
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> pShapeGetter) {
        return super.getShapeForEachState(pShapeGetter);
    }

    public LootChestBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LootChestBlockEntity(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if (blockEntity instanceof LootChestBlockEntity lootChestBlockEntity) {

                if (lootChestBlockEntity.hasLootTableToBeUnpacked()) {
                    lootChestBlockEntity.unpackLootTable(pLevel);
                }

                boolean open;
                if (pPlayer.isShiftKeyDown()) {
                    if (!pState.getValue(OPEN)) {
                        NetworkHooks.openScreen((ServerPlayer) pPlayer, lootChestBlockEntity, pPos);
                    }
                    if (pState.getValue(OPEN)) {
                        pLevel.playSound(null, pPos, ModSounds.CLOSE_LOOT_CHEST.get(), SoundSource.BLOCKS);
                    }
                    open = false;
                } else {
                    lootChestBlockEntity.popupItem();
                    open = true;
                    if (!pState.getValue(OPEN)) {
                        pLevel.playSound(null, pPos, ModSounds.OPEN_LOOT_CHEST.get(), SoundSource.BLOCKS);
                    }
                }
                pLevel.setBlock(pPos, pState.setValue(OPEN, open), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
