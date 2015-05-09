package pt.uptodate.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import pt.uptodate.FetchedUpdateable;
import pt.uptodate.UpToDate;
import pt.uptodate.gui.GuiUpdates;

import java.awt.*;
import java.util.ArrayList;

public class GuiHandler implements IGuiHandler {
	private boolean mainLaunched = false;
	private ArrayList<FetchedUpdateable> updates;
	private String returnMessage;
	private String updatesAvailable;
	private Object modObj;

	public GuiHandler(ArrayList<FetchedUpdateable> updates, String returnMessage, String updatesAvailable, Object modObj) {
		this();
		this.updates = updates;
		this.returnMessage = returnMessage;
		this.updatesAvailable = updatesAvailable;
		this.modObj = modObj;
	}

	public GuiHandler() {
		super();
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
		if (event.gui instanceof GuiMainMenu) {
			String most = "";
			Integer mostSevere = null;
			if (UpToDate.updates.size() > 0) {
				boolean critOrSevere = (!UpToDate.updates.getSevere().isEmpty() || !UpToDate.updates.getCritical().isEmpty());
				if (critOrSevere && !mainLaunched && Config.severe) {
					GuiScreen screen = new GuiUpdates(UpToDate.updates, "To Main Menu", "There are severe and/or critical updates available:");
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}

				if (!UpToDate.updates.getCritical().isEmpty()) {
					if (Config.colorblind) {
						most = " (!!)";
					}
					mostSevere = Color.RED.getRGB();
				} else if (!UpToDate.updates.getSevere().isEmpty()) {
					if (Config.colorblind) {
						most = " (!)";
					}
					mostSevere = Color.YELLOW.getRGB();
				}
			}

			GuiButton button = new GuiButton(10, 10, 0, "Updates " + "(" + UpToDate.updates.size() + ")" + most) {
				@Override
				public void mouseReleased(int p_146118_1_, int p_146118_2_) {
					GuiScreen screen = new GuiUpdates(UpToDate.updates, "Return", "Available updates:");
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}
			};
			if (mostSevere != null && !Config.colorblind)
				button.packedFGColour = mostSevere;
			button.yPosition = event.gui.height - 62;
			button.xPosition = event.gui.width/2 - button.width/4;
			button.height = 13;
			button.width =  98;
			event.buttonList.add(button);

			mainLaunched = true;
		}
	}

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
