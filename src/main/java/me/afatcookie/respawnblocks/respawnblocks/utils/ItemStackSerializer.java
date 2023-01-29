
package me.afatcookie.respawnblocks.respawnblocks.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer {

    /**
     * Serialize an ItemStack object into a byte array
     * @param itemStack The ItemStack object to be serialized
     * @return The serialized byte array of the ItemStack object
     */
    public static byte[] serialize(ItemStack itemStack){
        byte[] serializedObject = null;
        try {
            // create a new ByteArrayOutputStream
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            // create a new BukkitObjectOutputStream using the ByteArrayOutputStream
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            // write the itemStack object to the BukkitObjectOutputStream
            os.writeObject(itemStack);
            // flush the BukkitObjectOutputStream
            os.flush();

            // set the serializedObject variable to the byte array of the ByteArrayOutputStream
            serializedObject = io.toByteArray();
        } catch (IOException e) {
            // print the stack trace of the IOException
            e.printStackTrace();
        }
        return serializedObject;
    }



    /**
     * Deserializes a byte array into an ItemStack object
     * @param item The byte array to deserialize
     * @return The deserialized ItemStack object, or null if an error occurred
     */
    public static ItemStack deserialize(byte[] item) {
        ItemStack itemStack = null;
        try {
            // Create input streams from the byte array
            ByteArrayInputStream in = new ByteArrayInputStream(item);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            // Read the object from the input stream and cast it to an ItemStack
            itemStack = (ItemStack) is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // Print the stack trace of the error if it occurs
            e.printStackTrace();
        }
        return itemStack;
    }

}