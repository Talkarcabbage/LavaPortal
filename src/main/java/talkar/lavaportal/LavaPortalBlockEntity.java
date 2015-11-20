package talkar.lavaportal;

import net.minecraft.tileentity.TileEntity;

public class LavaPortalBlockEntity extends TileEntity{

	byte tickCounter = 0;
	byte validityTickCounter = 0;
	boolean isDestroyed = false;
	
	public LavaPortalBlockEntity() {
		super();
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public void setIsDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	
	@Override
	public void updateEntity() {
		if (tickCounter++ > LavaPortal.lavaSpawnTickRate) {
			LavaPortal.portalBlock.updateTick(this.worldObj, xCoord, yCoord, zCoord, this.worldObj.rand);
			tickCounter = 0;
		}
		if (validityTickCounter++ > 40) {
			LavaPortal.portalBlock.verifyPortalTick(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord,yCoord,zCoord));
			validityTickCounter = 0;
		}


	}
	@Override
	public boolean canUpdate() {
		return true;
	}
}
