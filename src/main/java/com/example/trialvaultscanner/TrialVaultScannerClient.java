package com.example.trialvaultscanner;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.Set;

public class TrialVaultScannerClient implements ClientModInitializer {

    private static final Set<ChunkPos> scannedChunks = new HashSet<>();
    private static final Set<BlockPos> foundBlocks = new HashSet<>();

    private static final int CHUNK_DISTANCE = 8;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) {
                return;
            }

            int playerChunkX = client.player.getChunkPos().x;
            int playerChunkZ = client.player.getChunkPos().z;

            for (int chunkX = playerChunkX - CHUNK_DISTANCE;
                 chunkX <= playerChunkX + CHUNK_DISTANCE;
                 chunkX++) {

                for (int chunkZ = playerChunkZ - CHUNK_DISTANCE;
                     chunkZ <= playerChunkZ + CHUNK_DISTANCE;
                     chunkZ++) {

                    ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

                    if (scannedChunks.contains(chunkPos)) {
                        continue;
                    }

                    WorldChunk chunk = client.world.getChunkManager()
                            .getWorldChunk(chunkX, chunkZ);

                    if (chunk == null) {
                        continue;
                    }

                    scannedChunks.add(chunkPos);
                    scanChunk(client, chunk);
                }
            }
        });
    }

    private static void scanChunk(MinecraftClient client, WorldChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();

        int minY = client.world.getBottomY();
        int maxY = client.world.getTopY();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(
                            chunkPos.getStartX() + x,
                            y,
                            chunkPos.getStartZ() + z
                    );

                    Block block = chunk.getBlockState(pos).getBlock();

                    if (block == Blocks.TRIAL_SPAWNER || block == Blocks.VAULT) {
                        if (foundBlocks.add(pos)) {
                            String type;

                            if (block == Blocks.TRIAL_SPAWNER) {
                                type = "Trial Spawner";
                            } else {
                                type = "Vault";
                            }

                            client.player.sendMessage(
                                    Text.literal(
                                            "Found " + type + " at X: "
                                                    + pos.getX()
                                                    + " Y: " + pos.getY()
                                                    + " Z: " + pos.getZ()
                                    ),
                                    false
                            );
                        }
                    }
                }
            }
        }
    }
}
