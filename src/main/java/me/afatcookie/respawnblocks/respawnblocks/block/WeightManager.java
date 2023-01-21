package me.afatcookie.respawnblocks.respawnblocks.block;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightManager {



    private final Map<Reward, Integer> weightedItems;

    private final RespawnBlock respawnBlock;

    private final Random randomNumber;

    public WeightManager(RespawnBlock block){
        weightedItems = new HashMap<>();
        this.respawnBlock = block;
        for (Reward reward : block.getRewards()){
            weightedItems.put(reward, reward.getWeight());
        }
        randomNumber = new Random();
    }


    /**
     * WHEN USING THIS METHOD, THE HIGHER THE NUMBER, THE BETTER CHANCE IT CAN BE PICKED
     * @return the reward based on the chance
     */
    public Reward getWeightedReward() {
        int totalWeight = 0;
        // Sum up all the weights of the rewards
        for (int weight : weightedItems.values()) {
            totalWeight += weight;
        }
        // If total weight is less than or equal to 0, return a default reward with a block of the same type as the respawn block and a 50% chance
        if (totalWeight <= 0) {
            return new Reward(respawnBlock.getBlockID(), new ItemStack(respawnBlock.getInitialBlockType(), 1), 50);
        }
        int random = randomNumber.nextInt(totalWeight);
        // Iterate through the map of rewards and their weights
        for (Map.Entry<Reward, Integer> entry : weightedItems.entrySet()) {
            random -= entry.getValue(); // subtract the weight of the current reward from the random number
            if (random <= 0) {
                return entry.getKey(); // return the reward if the random number is less than or equal to 0
            }
        }
        return null; // if no reward is selected, return null
    }

}
