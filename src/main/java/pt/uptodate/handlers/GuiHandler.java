package pt.uptodate.handlers;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import pt.uptodate.FetchedUpdateable;
import pt.uptodate.UpToDate;
import pt.uptodate.gui.GuiUpdates;
import pt.uptodate.util.Util;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler {
	private boolean mainLaunched = false;

	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
		if (event.gui instanceof GuiMainMenu) {
			Integer mostSevere = null;
			if (UpToDate.updates.size() > 0) {
				if (!UpToDate.updates.severe.isEmpty() && !mainLaunched && Config.severe) {
					GuiScreen screen = new GuiUpdates(UpToDate.updates, "To Main Menu", "There are severe and/or critical updates available:");
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}

				if (!UpToDate.updates.critical.isEmpty()) {
					mostSevere = Color.RED.getRGB();
				} else if (!UpToDate.updates.severe.isEmpty()) {
					mostSevere = Color.YELLOW.getRGB();
				}
			}

			GuiButton button = new GuiButton(10, 10, 0, "Updates " + "(" + UpToDate.updates.size() + ")") {
				@Override
				public void mouseReleased(int p_146118_1_, int p_146118_2_) {
					GuiScreen screen = new GuiUpdates(UpToDate.updates, "Return", "Available updates:");
					Minecraft.getMinecraft().displayGuiScreen(screen);
				}
			};
			if (mostSevere != null)
				button.packedFGColour = mostSevere;
			button.yPosition = event.gui.height - 62;
			button.xPosition = event.gui.width/2 - button.width/4;
			button.height = 13;
			button.width =  98;
			event.buttonList.add(button);

			mainLaunched = true;
		}
	}
}
