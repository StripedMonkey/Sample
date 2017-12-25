/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.Sample.blocks;

import gnu.trove.map.TByteObjectMap;
import org.terasology.math.Side;
import org.terasology.math.SideBitFlag;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.UpdatesWithNeighboursFamily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoneColumnFamily extends UpdatesWithNeighboursFamily {
    private TByteObjectMap<Block> blocks;

    public StoneColumnFamily(BlockUri blockUri, List<String> categories,
                             Block archetypeBlock, TByteObjectMap<Block> blocks) {
        super(null, blockUri, categories, archetypeBlock, blocks, (byte) 63);

        this.blocks = blocks;
    }

    @Override
    public Block getBlockForPlacement(WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry,
                                      Vector3i location, Side attachmentSide, Side direction) {
        return getBlockForLocation(worldProvider, location);
    }

    @Override
    public Block getBlockForNeighborUpdate(WorldProvider worldProvider,
                                           BlockEntityRegistry blockEntityRegistry,
                                           Vector3i location, Block oldBlock) {
        return getBlockForLocation(worldProvider, location);
    }

    private Block getBlockForLocation(WorldProvider worldProvider, Vector3i location) {
        Set<Side> stoneColumnNeighborSides = new HashSet<>();

        for (Side side : new Side[] {Side.TOP, Side.BOTTOM}) {
            Vector3i neighborLocation = new Vector3i(location);
            neighborLocation.add(side.getVector3i());

            if (!worldProvider.isBlockRelevant(neighborLocation)) {
                continue;
            }

            Block neighborBlock = worldProvider.getBlock(neighborLocation);
            final BlockFamily blockFamily = neighborBlock.getBlockFamily();

            if (blockFamily instanceof StoneColumnFamily) {
                stoneColumnNeighborSides.add(side);
            }
        }

        return blocks.get(SideBitFlag.getSides(stoneColumnNeighborSides));
    }
}