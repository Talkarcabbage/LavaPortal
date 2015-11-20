package talkar.lavaportal;

import net.minecraft.world.World;

public class LavaPortalUtil {

	public static String getWorldCoordinatesForLogging(World world, int x, int y, int z) {
		return ("Dimension ID: " + world.provider.dimensionId + " :: Coordinates: " + x + "," + y + "," + z);
	}
	
}
