package pt.uptodate.gui;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import pt.uptodate.FetchedUpdateable;
import pt.uptodate.UpToDate;
import pt.uptodate.util.io.WebUtils;

/**
 * @author CoolSquid
 */

public class GuiUpdates extends GuiBase {
	private final Iterable<FetchedUpdateable> updates;
	private final String returnMessage;
	private final String updatesAvailable;

	public GuiUpdates(Iterable<FetchedUpdateable> updates, String returnMessage, String updatesAvailable) {
		super();
		this.updates = updates;
		this.returnMessage = returnMessage;
		this.updatesAvailable = updatesAvailable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int a = 0;
		for (FetchedUpdateable fetched : updates) {
			GuiButton b = new GuiButtonMod(1, 0, this.height / 2 - 20 + a, fetched.mod.getName() + " " + fetched.oldDisp + "/" + fetched.display, fetched);
			b.xPosition = this.width / 2 - (b.width / 2);
			this.buttonList.add(b);
			a += b.height + 4;
		}
		GuiButton button = new GuiButton(0, 10, 0, returnMessage);
		button.yPosition = this.height - button.height - 10;
		button.width = 100;
		this.buttonList.add(button);
	}

	@Override
	public void drawScreen(int mouseRelX, int mouseRelY, float tickTime) {
		this.drawDefaultBackground();
		super.drawScreen(mouseRelX, mouseRelY, tickTime);
		if (UpToDate.updates.isEmpty()) {
			this.drawString("No updates available.", this.width / 2, this.height / 2);
		}
		else {
			String line1 = updatesAvailable;
			this.drawCenteredString(this.fontRendererObj, line1, this.width / 2, this.height / 2 - 60, 16777215);
		}
		if (!Desktop.isDesktopSupported()) {
			this.drawString("Your desktop is not supported. The buttons will not be clickable.", this.width / 2, 20);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			if (Desktop.isDesktopSupported()) {
				WebUtils.openBrowser(((GuiButtonMod) button).getMod().url);
			}
		}
		else if (button.id == 0) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	private static class GuiButtonMod extends GuiButton {

		private final FetchedUpdateable mod;

		public GuiButtonMod(int id, int x, int y, String name, FetchedUpdateable mod) {
			super(id, x, y, name);
			if (mod.severity >= 3) {
				this.packedFGColour = Color.RED.getRGB();
			} else if (mod.severity >= 2) {
				this.packedFGColour = Color.YELLOW.getRGB();
			}
			this.mod = mod;
		}

		public FetchedUpdateable getMod() {
			return this.mod;
		}
	}
}