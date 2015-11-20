package talkar.lavaportal;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class NetherLens extends Item {
	public NetherLens() {
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("netherLens");
		this.setTextureName("lavaportal:netherLens");
	}
}
