package net.applebyfamily.freethemice;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class FM_GuiHandler implements IGuiHandler{

	public FM_Gui _MyGui;
	public FM_GuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(FinderMod.instance, this);
		_MyGui = new FM_Gui();
		
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 0:
				return _MyGui;
		default:
			return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 0:			
			return _MyGui;
		default:
			return null;
		}
	}

}
