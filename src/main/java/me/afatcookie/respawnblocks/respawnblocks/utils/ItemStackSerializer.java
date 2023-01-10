
package me.afatcookie.respawnblocks.respawnblocks.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer {



    public static byte[] serialize(ItemStack itemStack){
        byte[] serializedObject = null;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(itemStack);
            os.flush();

            serializedObject = io.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedObject;
    }


    public static ItemStack deserialize(byte[] item){
        ItemStack itemStack = null;
        try {

            ByteArrayInputStream in = new ByteArrayInputStream(item);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
             itemStack = (ItemStack) is.readObject();
        }catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        return itemStack;
    }
}