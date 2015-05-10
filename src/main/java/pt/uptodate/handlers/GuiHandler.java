package pt.uptodate.handlers;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import pt.uptodate.FetchedUpdateable;
import pt.uptodate.gui.GuiUpdates;

import java.util.ArrayList;

public class GuiHandler implements IGuiHandler {
	private ArrayList<FetchedUpdateable> updates;
	private String returnMessage;
	private String updatesAvailable;

	/**
	 * Constructor for in-game gui
	 * @param updates updates to use.
	 * @param returnMessage return message to use
	 * @param updatesAvailable message to use if updates are available
	 */
	public GuiHandler(ArrayList<FetchedUpdateable> updates, String returnMessage, String updatesAvailable) {
		this.updates = updates;
		this.returnMessage = returnMessage;
		this.updatesAvailable = updatesAvailable;
	}

	/**
	 * Gets the server Gui Container
	 * @param ID id of gui
	 * @param player player accessing
	 * @param world world accessed in
	 * @param x what x pos accessed at
	 * @param y what y pos accessed at
	 * @param z what z pos accessed at
	 * @return Container Forge will use
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GuiUpdates.GUI_ID) {
			return new Container() {
				@Override
				public boolean canInteractWith(EntityPlayer p_75145_1_) {
					return false;
				}
			};
		}
		return null;
	}

	/**
	 * Gets the client side gui
	 * @param ID id of gui
	 * @param player player accessing
	 * @param world world accessed in
	 * @param x what x pos accessed at
	 * @param y what y pos accessed at
	 * @param z what z pos accessed at
	 * @return GuiScreen forge will use
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GuiUpdates.GUI_ID) {
			if (world.isRemote) {
				return new GuiUpdates(updates, returnMessage, updatesAvailable);
			} else {
				getServerGuiElement(ID, player, world, x, y, z);
			}
		}
		return null;
	}
}
