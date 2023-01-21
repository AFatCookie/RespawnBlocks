package me.afatcookie.respawnblocks.respawnblocks.DB;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemStackSerializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/*
Database class that allows access to save active blocks on server restarts/closes.
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class Database {
    RespawnBlocks plugin;
    Connection connection;

    //Creates Active Respawn blocks table, holding the blockID, the X, Y, and Z coord. It also holds the initial
    // material of the block, when created into a respawn block, and the world it was made in as a string. Each of these
    //are sorted in the sense that they follow the corresponding blockID.
    private final String CREATE_ACTIVE_BLOCKS_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "blockID INT  NOT NULL," +
            "xcoord SMALLINT DEFAULT 0 NOT NULL," +
            "ycoord SMALLINT DEFAULT 0 NOT NULL," +
            "zcoord SMALLINT DEFAULT 0 NOT NULL," +
            "material VARCHAR(30) DEFAULT '' NOT NULL," +
            "world VARCHAR(30) DEFAULT '' NOT NULL," +
            "PRIMARY KEY (blockID)" +
            ");";

    //Creates the block ids table, holding idBlock.
    private final String CREATE_BLOCK_ID_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "idBlock INT NOT NULL" + ");";

    //Creates the reward table, using a blockid as the primary key.
    private final String CREATE_REWARD_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "blockID INT  NOT NULL," +
            "item BLOB NOT NULL," +
            "weight INT  NOT NULL" +
            ");";

   //Will put the blockID, x y and z coord, initial material, and the world as a string into the table.
    private final String SAVE_INTO_TABLE = "INSERT OR REPLACE INTO {table_name}(blockID, xcoord, ycoord, zcoord, material, world) VALUES(?,?,?,?,?,?);";

    //Will put the blockID into the table.
    private final String SAVE_INTO_ID_TABLE = "INSERT OR REPLACE INTO {table_name}(idBlock) VALUES(?);";

    //Will save rewards into the table
    private final String SAVE_INTO_REWARDS_TABLE = "INSERT OR REPLACE INTO {table_name}(blockID, item, weight) VALUES(?,?,?);";

    //Lets you completely clear a table
    private final String DELETE = "DELETE FROM {table_name};";
    //This will get the data that corresponds to the blockID.
    private final String LOAD_DATA_FROM_BLOCK_TABLE = "SELECT * FROM {table_name} WHERE blockID = ?;";

    //Selects all from the table.
    private final String LOAD_DATA_FROM_BLOCKID_TABLE = "SELECT * FROM {table_name};";

    //Get all data from rewards table
    private final String LOAD_DATA_FROM_REWARD_TABLE = "SELECT * FROM {table_name} WHERE blockID = ?;";


    //DELETES SPECIFIED TABLE
    private final String DELETE_TABLE = "DROP TABLE {table_name};";

    public Database(RespawnBlocks instance) {
        plugin = instance;
    }


    public abstract Connection getSQLConnection();


    /**
     * Method which creates the ActiveBlocks table. Is ran in the onEnable.
     */
    public void createActiveBlocksTable(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_ACTIVE_BLOCKS_TABLE.replace("{table_name}", "activeblocks"));
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * Method which creates the BlockID table. Ran in the onEnable.
     */
    public void createBlockIDTable(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_BLOCK_ID_TABLE.replace("{table_name}", "idtable"));
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * Creates Reward table
     */
    public void createRewardTable(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_REWARD_TABLE.replace("{table_name}", "rewards"));
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * This will save the respawn block to the activeblocks table, using its information provided.
     * @param respawnBlock the respawnBlock to save into the activeblocks table.
     */
    public void saveActiveBlocksToTable(RespawnBlock respawnBlock){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(SAVE_INTO_TABLE.replace("{table_name}", "activeblocks"));
            ps.setInt(1, respawnBlock.getBlockID());
            ps.setInt(2, respawnBlock.getX());
            ps.setInt(3, respawnBlock.getY());
            ps.setInt(4, respawnBlock.getZ());
            ps.setString(5, respawnBlock.getInitialBlockType().toString());
            ps.setString(6, respawnBlock.getWorld().getName());
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * This will save the respawnBlocks id into idtable
     * @param respawnBlock the respawn to get the id of.
     */
    public void saveIDSToTable(RespawnBlock respawnBlock){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(SAVE_INTO_ID_TABLE.replace("{table_name}", "idtable"));
            ps.setInt(1, respawnBlock.getBlockID());
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * Saves the rewards into the table
     * @param respawnBlock respawnBlock to check
     */
    public void saveRewardsToTable(RespawnBlock respawnBlock, byte[] itemStack, int weight){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(SAVE_INTO_REWARDS_TABLE.replace("{table_name}", "rewards"));
            ps.setInt(1, respawnBlock.getBlockID());
            ps.setBytes(2, itemStack);
            ps.setInt(3, weight);
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * this will completely clear the specified table of all its data.
     * @param table the table to access.
     */
    public void clearTable(String table){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE.replace("{table_name}", table));
            ps.execute();
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
    }

    /**
     * This will use the blockID to identify the row, and then get the values that correspond to the blockID. With those
     * values, it will create a RespawnBlock object, and return it. This is used in RespawnBlockManager, and is being put
     * back into the ArrayList of all active RespawnBlocks.
     * @param blockID The blockID to check the table for.
     * @return The RespawnBlock which was created from the information that corresponds to the blockID.
     */
    public RespawnBlock getActiveBlocks(int blockID){
        Connection conn = null;
        PreparedStatement ps = null;
        RespawnBlock rb = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA_FROM_BLOCK_TABLE.replace("{table_name}", "activeblocks"));
            ps.setInt(1, blockID);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
             rb = new RespawnBlock(resultSet.getInt(2), resultSet.getInt(3),
                     resultSet.getInt(4), plugin, blockID, resultSet.getString(6), resultSet.getString(5));
            }
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);

        }finally{
            close(ps, conn);
        }
        return rb;
    }

    /**
     * This method takes everything from the ids table, and puts it all into an ArrayList. The ArrayList is then returned
     * which is used to get the RespawnBlocks, and to find where the appropriate starting point of the next blockID Should be.
     * @return the ArrayList of all blockids.
     */
    public ArrayList<Integer> getBlockIDS(){
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<Integer> ids = new ArrayList<>();
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA_FROM_BLOCKID_TABLE.replace("{table_name}", "idtable"));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                ids.add(resultSet.getInt(1));
            }
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);

        }finally{
            close(ps, conn);
        }
        return ids;
}

    /**
     * This method will get the rewards that match the corresponding id
     * @param blockID the blockid to check
     * @return the arraylist of rewards that match the blockID.
     */
    public ArrayList<Reward> getRewardsFromDB(int blockID){
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<Reward> rewardArrayList = new ArrayList<>();
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA_FROM_REWARD_TABLE.replace("{table_name}", "rewards"));
            ps.setInt(1, blockID);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                rewardArrayList.add(new Reward(blockID, ItemStackSerializer.deserialize(resultSet.getBytes(2)), resultSet.getInt(3)));
            }
        }catch (SQLException ex){
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }finally{
            close(ps, conn);
        }
        return rewardArrayList;
    }


    public void close(PreparedStatement ps, Connection cn) {
        try {
            if (ps != null)
                ps.close();
            if (cn != null)
                cn.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}