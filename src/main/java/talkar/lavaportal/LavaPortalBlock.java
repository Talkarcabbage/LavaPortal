package talkar.lavaportal;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LavaPortalBlock extends BlockContainer {

	/**
	 * Guide to portal blocks: N W E S
	 * 
	 * 1 2
	 * 
	 * 3 4
	 * 
	 * @param material
	 */

	protected LavaPortalBlock(Material material) {
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(20.0f);
		this.setResistance(1600.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockName("portalBlock");
		this.setBlockTextureName("lavaportal:lavaPortalBlock");
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int a, float b, float c, float d) {

		if (player.isSneaking()) {
			if (LavaPortal.debugInChat) {
				player.addChatMessage(new ChatComponentText("Current Metadata: " + world.getBlockMetadata(x, y, z)));
				player.addChatMessage(new ChatComponentText("Current Location: " + x + "," + y + "," + z));
				player.addChatMessage(new ChatComponentText("Current Destroyed Flag:" + ((LavaPortalBlockEntity) (world.getTileEntity(x, y, z))).isDestroyed()));
				
			}

		} else if (world.getBlockMetadata(x, y, z)==0) {

			if (LavaPortalBlock.isPortalBlockValid(world, x, y, z, 1)) {
				if (LavaPortal.debugInChat) player.addChatMessage(new ChatComponentText("Initializing portal with corner ID 1"));
				world.playSoundEffect(x, y, z, "fire.ignite", 1.0F, 1F);
				world.setBlock(x, y, z, this, 1, 3);
				world.setBlock(x, y, z + 3, this, 3, 3);
				world.setBlock(x + 3, y, z, this, 2, 3);
				world.setBlock(x + 3, y, z + 3, this, 4, 3);

			} else if (LavaPortalBlock.isPortalBlockValid(world, x, y, z, 2)) {
				if (LavaPortal.debugInChat) player.addChatMessage(new ChatComponentText("Initializing portal with corner ID 2"));
				world.playSoundEffect(x, y, z, "fire.ignite", 1.0F, 1F);
				world.setBlock(x, y, z, this, 2, 3);
				world.setBlock(x, y, z + 3, this, 4, 3);
				world.setBlock(x - 3, y, z, this, 1, 3);
				world.setBlock(x - 3, y, z + 3, this, 3, 3);

			} else if (LavaPortalBlock.isPortalBlockValid(world, x, y, z, 3)) {
				if (LavaPortal.debugInChat) player.addChatMessage(new ChatComponentText("Initializing portal with corner ID 3"));
				world.playSoundEffect(x, y, z, "fire.ignite", 1.0F, 1F);
				world.setBlock(x, y, z, this, 3, 3);
				world.setBlock(x, y, z - 3, this, 1, 3);
				world.setBlock(x + 3, y, z, this, 4, 3);
				world.setBlock(x + 3, y, z - 3, this, 2, 3);

			} else if (LavaPortalBlock.isPortalBlockValid(world, x, y, z, 4)) {
				if (LavaPortal.debugInChat) player.addChatMessage(new ChatComponentText("Initializing portal with corner ID 4"));
				world.playSoundEffect(x, y, z, "fire.ignite", 1.0F, 1F);
				world.setBlock(x, y, z, this, 4, 3);
				world.setBlock(x, y, z - 3, this, 2, 3);
				world.setBlock(x - 3, y, z, this, 3, 3);
				world.setBlock(x - 3, y, z - 3, this, 1, 3);

			} else {
				if (!world.isRemote && LavaPortal.debugInChat) player.addChatMessage(new ChatComponentText("Portal did not meet formation requirements."));

			}

		}
		return true;
	}

	@Override 
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (world.getBlockMetadata(x, y, z) > 0 && !isPortalBlockValid(world, x, y, z, world.getBlockMetadata(x, y, z))) {
			world.setBlock(x, y, z, this, 0, 3);
		}
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new LavaPortalBlockEntity();
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {

		switch (world.getBlockMetadata(x, y, z)) {
			case 1:
				if (canLavaSpawnAt(world, x + 1, y, z + 1)) {
					world.setBlock(x + 1, y, z + 1, Blocks.flowing_lava, 0 , 3);
				}
				break;
			case 2:
				if (canLavaSpawnAt(world, x - 1, y, z + 1)) {
					world.setBlock(x - 1, y, z + 1, Blocks.flowing_lava, 0 , 3);
				}
				break;
			case 3:
				if (canLavaSpawnAt(world, x + 1, y, z - 1)) {
					world.setBlock(x + 1, y, z - 1, Blocks.flowing_lava, 0 , 3);
				}
				break;
			case 4:
				if (canLavaSpawnAt(world, x - 1, y, z - 1)) {
					world.setBlock(x - 1, y, z - 1, Blocks.flowing_lava , 0 , 3);
				}
				break;
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int metaData) {
		if (metaData > 0) {
			TileEntity t = world.getTileEntity(x, y, z);
			if (t instanceof LavaPortalBlockEntity) {
				if (((LavaPortalBlockEntity) (t)).isDestroyed()) {
					return;
				} else {
					((LavaPortalBlockEntity) (t)).setIsDestroyed(true);
				}
			} else {
				LavaPortal.LOG
						.error("Expected LavaPortalBlockEntity TileEntity, but object is not an instance of this class. " + LavaPortalUtil.getWorldCoordinatesForLogging(world, x, y, z));
			}

			switch (metaData) {
				case 1:
					world.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1F);
					if (world.getBlock(x , y , z + 3) instanceof LavaPortalBlock) world.setBlock(x, y, z + 3, this, 0, 3);
					if (world.getBlock(x + 3 , y , z) instanceof LavaPortalBlock) world.setBlock(x + 3, y, z, this, 0, 3);
					if (world.getBlock(x + 3 , y , z + 3) instanceof LavaPortalBlock) world.setBlock(x + 3, y, z + 3, this, 0, 3);
					if (world.getBlock(x + 1, y, z + 1).equals(Blocks.lava) || world.getBlock(x + 1, y, z + 1).equals(Blocks.flowing_lava))
						world.setBlock(x + 1, y, z + 1, Blocks.air);
					break;

				case 2:
					world.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1F);
					if (world.getBlock(x , y , z + 3) instanceof LavaPortalBlock) world.setBlock(x, y, z + 3, this, 0, 3);
					if (world.getBlock(x - 3 , y , z) instanceof LavaPortalBlock) world.setBlock(x - 3, y, z, this, 0, 3);
					if (world.getBlock(x - 3 , y , z + 3) instanceof LavaPortalBlock) world.setBlock(x - 3, y, z + 3, this, 0, 3);
					if (world.getBlock(x - 1, y, z + 1).equals(Blocks.lava) || world.getBlock(x - 1, y, z + 1).equals(Blocks.flowing_lava))
						world.setBlock(x - 1, y, z + 1, Blocks.air);
					break;

				case 3:
					world.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1F);
					if (world.getBlock(x , y , z - 3) instanceof LavaPortalBlock) world.setBlock(x, y, z - 3, this, 0, 3);
					if (world.getBlock(x + 3 , y , z) instanceof LavaPortalBlock) world.setBlock(x + 3, y, z, this, 0, 3);
					if (world.getBlock(x + 3 , y , z - 3) instanceof LavaPortalBlock) world.setBlock(x + 3, y, z - 3, this, 0, 3);
					if (world.getBlock(x + 1, y, z - 1).equals(Blocks.lava) || world.getBlock(x + 1, y, z - 1).equals(Blocks.flowing_lava))
						world.setBlock(x + 1, y, z - 1, Blocks.air);
					break;

				case 4:
					world.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1F);
					if (world.getBlock(x , y , z - 3) instanceof LavaPortalBlock) world.setBlock(x, y, z - 3, this, 0, 3);
					if (world.getBlock(x - 3 , y , z) instanceof LavaPortalBlock) world.setBlock(x - 3, y, z, this, 0, 3);
					if (world.getBlock(x - 3 , y , z - 3) instanceof LavaPortalBlock) world.setBlock(x - 3, y, z - 3, this, 0, 3);
					if (world.getBlock(x - 1, y, z - 1).equals(Blocks.lava) || world.getBlock(x - 1, y, z - 1).equals(Blocks.flowing_lava))
						world.setBlock(x - 1, y, z - 1, Blocks.air);
					break;

			}

		} else {
			((LavaPortalBlockEntity) (world.getTileEntity(x, y, z))).setIsDestroyed(false);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random rand) {
		if (world.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
			for (int l = 0; l < 4; ++l) {
				double x = (double) ((float) xCoord + rand.nextFloat());
				double y = (double) ((float) yCoord + rand.nextFloat());
				double z = (double) ((float) zCoord + rand.nextFloat());
				double vecX = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double vecY = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double vecZ = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				
				world.spawnParticle("portal", x, y + 1, z, vecX, vecY, vecZ);
			}
		}
	}

	/**
	 * 
	 * @param world - The world object the block is in.
	 * @param x - The X coordinate of the block.
	 * @param y - The Y coordinate of the block.
	 * @param z - The Z coordinate of the block.
	 * @param cornerID - The Corner ID of the block, which is a number between 1 and 4 representing which corner NW NE SW SE, in that order, the block is relative to the portal structure.
	 * @return True if the block is a valid portal corner relative to the other 3 blocks and the obsidian frame, false if a condition is not met.
	 */
	public static boolean isPortalBlockValid(World world, int x, int y, int z, int cornerID) {
		// @Formatter:off
		switch (cornerID) {
			
			case 0: {
				// This may be able to return case 1 2 3 or 4, though we shouldn't need this case ever. 
				// This should not be called normally and will add an appropriate log message.
				LavaPortal.LOG.error("A validity check was performed on a block with a cornerID of 0! This is an error! " + LavaPortalUtil.getWorldCoordinatesForLogging(world, x, y, z));
				
				break;
			}
			case 1: { //Northwest
				if (
					world.getBlock(x, y, z + 3)  instanceof LavaPortalBlock 
					&& world.getBlock(x + 3, y, z) instanceof LavaPortalBlock 
					&& world.getBlock(x + 3, y, z + 3) instanceof LavaPortalBlock
					
					&& (world.getBlock(x , y , z + 1)) instanceof BlockObsidian
					&& (world.getBlock(x , y , z + 2)) instanceof BlockObsidian
				
					&& (world.getBlock(x + 1, y , z)) instanceof BlockObsidian
					&& (world.getBlock(x + 2, y , z)) instanceof BlockObsidian
				
					&& (world.getBlock(x + 3, y, z + 1)) instanceof BlockObsidian
					&& (world.getBlock(x + 3, y, z + 2)) instanceof BlockObsidian
					
					&& (world.getBlock(x + 1, y, z + 3)) instanceof BlockObsidian
					&& (world.getBlock(x + 2, y, z + 3)) instanceof BlockObsidian
					) {
				
					return true;
				}				

				break;
			}

			case 2: { //Northeast
				if (
					world.getBlock(x, y, z + 3) instanceof LavaPortalBlock
					&& world.getBlock(x - 3, y, z) instanceof LavaPortalBlock
					&& world.getBlock(x - 3, y, z + 3) instanceof LavaPortalBlock
					
					&& world.getBlock(x , y , z + 1) instanceof BlockObsidian
					&& world.getBlock(x , y , z + 2) instanceof BlockObsidian
				
					&& world.getBlock(x - 1, y , z) instanceof BlockObsidian
					&& world.getBlock(x - 2, y , z) instanceof BlockObsidian
				
					&& world.getBlock(x - 3, y, z + 1) instanceof BlockObsidian
					&& world.getBlock(x - 3, y, z + 2) instanceof BlockObsidian
					
					&& world.getBlock(x - 1, y, z + 3) instanceof BlockObsidian
					&& world.getBlock(x - 2, y, z + 3) instanceof BlockObsidian
						) {
					return true;
				}

				break;
			}
			case 3: { //Southwest
				if (
					world.getBlock(x, y, z - 3) instanceof LavaPortalBlock 
					&& world.getBlock(x + 3, y, z) instanceof LavaPortalBlock
					&& world.getBlock(x + 3, y, z - 3) instanceof LavaPortalBlock
					
					&& world.getBlock(x , y , z - 1) instanceof BlockObsidian
					&& world.getBlock(x , y , z - 2) instanceof BlockObsidian
				
					&& world.getBlock(x + 1, y , z) instanceof BlockObsidian
					&& world.getBlock(x + 2, y , z) instanceof BlockObsidian
				
					&& world.getBlock(x + 3, y, z - 1) instanceof BlockObsidian
					&& world.getBlock(x + 3, y, z - 2) instanceof BlockObsidian
					
					&& world.getBlock(x + 1, y, z - 3) instanceof BlockObsidian
					&& world.getBlock(x + 2, y, z - 3) instanceof BlockObsidian
						
						) {
					return true;
				}

				break;
			}

			case 4: { //Southeast
				if (
					world.getBlock(x, y, z - 3) instanceof LavaPortalBlock
					&& world.getBlock(x - 3, y, z) instanceof LavaPortalBlock
					&& world.getBlock(x - 3, y, z - 3) instanceof LavaPortalBlock
					
					&& world.getBlock(x , y , z - 1) instanceof BlockObsidian
					&& world.getBlock(x , y , z - 2) instanceof BlockObsidian
				
					&& world.getBlock(x - 1, y , z) instanceof BlockObsidian
					&& world.getBlock(x - 2, y , z) instanceof BlockObsidian
				
					&& world.getBlock(x - 3, y, z - 1) instanceof BlockObsidian
					&& world.getBlock(x - 3, y, z - 2) instanceof BlockObsidian
					
					&& world.getBlock(x - 1, y, z - 3) instanceof BlockObsidian
					&& world.getBlock(x - 2, y, z - 3) instanceof BlockObsidian
						
						) {
					return true;
				}

				break;
			}

			default:
				LavaPortal.LOG.error("Error checking Lava Portal Block Validity: Block has invalid metadata value! " + LavaPortalUtil.getWorldCoordinatesForLogging(world, x, y, z));
				break;
		}
		// @Formatter:on
		
		return false;
	}
	
	public static boolean canLavaSpawnAt(World world, int x, int y, int z) {
		Block block = world.getBlock(x,y,z);
		if (block.isAir(world, x, y, z) || block == Blocks.lava || block == Blocks.flowing_lava) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks isPortalBlockValid for the current cornerID, should it be above 0, and sets the block metadata to 0 if it is invalidated.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param cornerID
	 */
	public static void verifyPortalTick(World world, int x, int y, int z, int cornerID) {
		if (world.getBlockMetadata(x, y, z) > 0 && !isPortalBlockValid(world, x, y, z, world.getBlockMetadata(x, y, z))) {
			world.setBlock(x, y, z, LavaPortal.portalBlock, 0, 3);
		} 
	}
	
}
