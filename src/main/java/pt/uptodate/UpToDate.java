package pt.uptodate;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import pt.uptodate.api.IUpdateable;
import pt.uptodate.api.UpdateableUtils;
import pt.uptodate.gui.GuiUpdates;
import pt.uptodate.handlers.Config;
import pt.uptodate.handlers.GuiHandler;
import pt.uptodate.util.Logger;
import pt.uptodate.util.Util;

import java.util.HashMap;
import java.util.Iterator;

@Mod(modid = UpToDate.MOD_ID, version = UpToDate.VERSION, name = UpToDate.MOD_NAME)
public class UpToDate implements IUpdateable
{
	public static final String MOD_ID = "uptodate";
	public static final String MOD_NAME = "UpToDate";
	public static final String VERSION = "1.0";
	public static final String SIMPLE_VERSION = "1";

	public static FetchedList updates = new FetchedList();
	protected static HashMap<EntityPlayer, Boolean> chatted = new HashMap<EntityPlayer, Boolean>();

	@EventHandler void pre(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Config());
		MinecraftForge.EVENT_BUS.register(new GuiHandler());

		GuiHandler guiHandler = new GuiHandler(UpToDate.updates.getCritical(), "To Game", "There are critical updates available, the old versions are world-breaking");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void complete(FMLLoadCompleteEvent event) {
		if (Util.netIsAvailable()) {
			for (ModContainer mod : Loader.instance().getActiveModList()) {
				if (mod.getMod() instanceof IUpdateable) {
					registerUpdateable((IUpdateable) mod.getMod());
				}
			}
			StringBuilder b = new StringBuilder();
			Iterator<FetchedUpdateable> i = updates.iterator();
			while (i.hasNext()) {
				FetchedUpdateable fetched = i.next();
				if (i.hasNext()) {
					b.append(", ");
				}
				else {
					b.append(" and ");
				}
				b.append(fetched.name);
			}
			Logger.info("The following mods are out of date: " + b.substring(2));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP && FMLCommonHandler.instance().getSide().equals(Side.SERVER)) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;

			boolean criticals = !updates.getCritical().isEmpty();
			boolean singlePlayer = player.mcServer.isSinglePlayer();
			boolean isOp = player.mcServer.getOpPermissionLevel() <= 2;
			if (criticals && (isOp || singlePlayer)) {
				if (Config.chat && (chatted.get(player) == null || !chatted.get(player))) {
					ChatComponentText text = new ChatComponentText("There are critical updates available, the old versions are world-breaking, click the mod name to download the update: ");

					for (FetchedUpdateable fetched : updates.critical) {
						ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, fetched.url);
						ChatStyle style = new ChatStyle().setChatClickEvent(click);
						ChatComponentText modText = new ChatComponentText(fetched.name + "");
						modText.setChatStyle(style);
						text.appendSibling(modText);
					}

					if (Config.colorblind) {
						text.appendSibling(new ChatComponentText("!!!"));
					} else {
						text.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
					}

					player.addChatComponentMessage(text);

					chatted.put(player, true);
				} else {
					if (!UpToDate.updates.getCritical().isEmpty()) {
						player.openGui(this, GuiUpdates.GUI_ID, player.worldObj, player.serverPosX, player.serverPosY, player.serverPosZ);
					}
				}
			}
		}
	}


	@Override
	public String getName() {
		return MOD_NAME;
	}

	@Override
	public String getRemote() {
		return UpdateableUtils.fromUrlToText("https://raw.githubusercontent.com/PhoenixTeamMC/UpToDate/master/version.json");
	}

	@Override
	public HashMap<String, String> getLocal() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("technical", String.valueOf(SIMPLE_VERSION));
		result.put("display", VERSION);
		return result;
	}

	/**
	 * Registers an updateable to uptodate's registries
	 * @param updateable the IUpdateable to register
	 */
	public static void registerUpdateable(IUpdateable updateable) {
		registerFetched(new FetchedUpdateable(updateable));
	}

	/**
	 * Registers a FetchedUpdateable to uptodate's registries
	 * @param fetched the FetchedUpdateable to register
	 */
	public static void registerFetched(FetchedUpdateable fetched) {
		boolean noNulls = !Util.anyNulls(fetched.display, fetched.displaySeverity, fetched.oldDisp, fetched.name, fetched.url);

		if (noNulls) {
			if (fetched.old < fetched.version) {
				updates.add(fetched);
			}
		} else {
			Logger.error((fetched.name == null ? "A mod" : fetched.name) + " has null values");
		}
	}
}
