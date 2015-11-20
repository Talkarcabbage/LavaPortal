package talkar.lavaportal;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;



@Mod(modid = LavaPortal.MODID, version = LavaPortal.VERSION, name = LavaPortal.NAME)
public class LavaPortal
{
	//Mod information
    public static final String MODID = "lavaportal";
    public static final String VERSION = "1.2.0";
    public static final String NAME = "Lava Portal";
    
    //Logger
	public static Logger LOG = LogManager.getLogger(MODID);
    
	//Blocks and Items
    public static LavaPortalBlock portalBlock;
    public static Item netherLens;
    
    //Configuration
    public static boolean aggressiveVerifyPortals;
    public static boolean debugInChat;
    public static int lavaSpawnTickRate; 
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration c = new Configuration(event.getSuggestedConfigurationFile());
    	
    	c.load();
    	
		aggressiveVerifyPortals = c.getBoolean("Aggressive Portal Validity Verification", "Settings", true,	"This setting determines whether or not the Lava Portal will regularly verify the validity of the structure even without block updates. Set this to true to prevent 'cheating' in the possibility the portal block and TileEntity is moved intact without causing a block update event. If many, many portals are in use, this setting can be disabled for marginal server performance.");
		debugInChat = c.getBoolean("Debug in Chat", "Settings", false, "If enabled, shift+right click will display information about a lava portal block in chat, and a message will be displayed when successfully or unsucessfully attempting to start a portal. This is useful primarily for debugging purposes.");
		lavaSpawnTickRate = c.getInt("Lava Spawn Rate", "Settings", 10, 1, Integer.MAX_VALUE, "Defines how often the portal block attempts to spawn a lava block, in ticks. Defaults to 10, which is twice per second. Higher values add more delay. Can be altered for difficulty or server performance.");
		
		
    	c.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
		// some example code
        LOG.info("Initializing Talkar's Lava Portal.");
    	
        portalBlock = new LavaPortalBlock(Material.rock);
        netherLens = new NetherLens();
        
        GameRegistry.registerBlock(portalBlock, "portalBlock");
        GameRegistry.registerItem(netherLens, "netherLens");
        GameRegistry.registerTileEntity(LavaPortalBlockEntity.class, "lavaPortalBlockEntity");
        
        GameRegistry.addShapedRecipe(new ItemStack(netherLens),
        		"SGB",
        		"GNG",
        		"TGC",
        		'S', new ItemStack(Blocks.soul_sand),
        		'G', new ItemStack(Blocks.glass),
        		'B', new ItemStack(Items.blaze_rod),
        		'N', new ItemStack(Blocks.netherrack),
        		'T', new ItemStack(Items.ghast_tear),
        		'C', new ItemStack(Items.fire_charge));
        
        GameRegistry.addShapedRecipe(new ItemStack(portalBlock, 4),
        		"OBO",
        		"PLP",
        		"OEO",
        		'O', new ItemStack(Blocks.obsidian),
        		'B', new ItemStack(Items.lava_bucket),
        		'P', new ItemStack(Items.ender_pearl),
        		'L', new ItemStack(netherLens),
        		'E', new ItemStack(Items.ender_eye)
        		
        		);
       
    }
}
