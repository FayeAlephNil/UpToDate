package pt.uptodate;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import pt.api.IUpdateable;
import pt.uptodate.handlers.Config;
import pt.uptodate.handlers.GuiHandler;
import pt.uptodate.util.Logger;
import pt.uptodate.util.ReflectionUtil;
import pt.uptodate.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = UpToDate.MOD_ID, version = UpToDate.VERSION, name = UpToDate.MOD_NAME)
public class UpToDate implements IUpdateable
{
    public static final String MOD_ID = "uptodate";
	public static final String MOD_NAME = "UpToDate";
    public static final String VERSION = "1.1";
	public static final int SIMPLE_VERSION = 2;

	public static ArrayList<FetchedUpdateable> updates = new ArrayList<FetchedUpdateable>();
	public static HashMap<EntityPlayer, Boolean> chatted = new HashMap<EntityPlayer, Boolean>();

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Config());
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
	}

	@EventHandler
	public void complete(FMLLoadCompleteEvent event) {
		if (Util.netIsAvailable()) {
			Object objMods = ReflectionUtil.getFieldValFromObj(Loader.instance(), "mods");
			Object objController = ReflectionUtil.getFieldValFromObj(Loader.instance(), "modController");

			if (objController instanceof LoadController && objMods instanceof List) {
				List mods = (List) objMods;
				Logger.info("Got mods list");
				for (Object mod : mods) {
					if (mod instanceof ModContainer && ((ModContainer) mod).getMod() instanceof IUpdateable) {
						IUpdateable modObj = (IUpdateable) ((ModContainer) mod).getMod();
						FetchedUpdateable toBe = new FetchedUpdateable(modObj);
						if (toBe.diff > 0) {
							updates.add(toBe);
						}
					}
				}
			}
			ArrayList<String> updateNames = new ArrayList<String>();
			for (FetchedUpdateable fetched : updates)
				updateNames.add(fetched.mod.getName());
			Logger.info("The following mods are out of date: " + Util.joinAnd(updateNames));
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (chatted.get(player) != null && !chatted.get(player) && player.worldObj.isRemote && !updates.isEmpty() && Config.chat) {
			ChatComponentText text = new ChatComponentText("There are critical updates available, click the mod name to download it: ");
			for (int i = 0; i < updates.size(); i++) {
				String ending = ", ";
				if (i + 1 == updates.size()) {
					ending = "";
				} else if (i + 2 == updates.size()) {
					ending = ", and ";
				}

				FetchedUpdateable fetched = updates.get(i);

				ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, fetched.url);
				ChatStyle style = new ChatStyle().setChatClickEvent(click);
				ChatComponentText modText = new ChatComponentText(fetched.mod.getName() + "");
				modText.setChatStyle(style);
				text.appendSibling(modText);
			}

			player.addChatComponentMessage(text);

			chatted.put(player, true);
		}
	}


	@Override
	public String getName() {
		return MOD_NAME;
	}

	@Override
	public String getRemote() {
		String result = null;
		try {
			ContentsService service = new ContentsService();
			Repository repo = (new RepositoryService()).getRepository("PhoenixTeamMC", "UpToDate");
			List codedContents = service.getContents(repo, "version.json");
			RepositoryContents contents = (RepositoryContents) codedContents.get(0);
			byte[] decoded = Base64.decodeBase64(contents.getContent());
			result = new String(decoded);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public HashMap<String, String> getLocal() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("technical", String.valueOf(SIMPLE_VERSION));
		result.put("display", VERSION);
		return result;
	}
}
