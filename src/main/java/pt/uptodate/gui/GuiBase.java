package pt.uptodate.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author CoolSquid
 */

@SideOnly(Side.CLIENT)
public class GuiBase extends GuiScreen {

	/**
	 * Draws the screen
	 * @param mouseRelX relative x
	 * @param mouseRelY relative y
	 * @param tickTime tickTime
	 */
	@Override
	public void drawScreen(int mouseRelX, int mouseRelY, float tickTime) {
		this.drawDefaultBackground();
		super.drawScreen(mouseRelX, mouseRelY, tickTime);
	}

	/**
	 * Draws a centered string
	 * @param string the string to draw
	 * @param x x to draw it at
	 * @param y y to draw it at
	 */
	public void drawString(String string, int x, int y) {
		this.drawCenteredString(this.fontRendererObj, string, x, y, 16777215);
	}
}