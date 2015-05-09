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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import pt.api.IUpdateable;
import pt.api.UpdateableUtils;
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
	private static HashMap<EntityPlayer, Boolean> chatted = new HashMap<EntityPlayer, Boolean>();

	@EventHandler void pre(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Config());
		MinecraftForge.EVENT_BUS.register(new GuiHandler());

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
				b.append(fetched.mod.getName());
			}
			Logger.info("The following mods are out of date: " + b.substring(2));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;

			boolean chattedBools = (chatted.get(player) == null || !chatted.get(player));
			boolean criticals = !updates.getCritical().isEmpty();
			boolean singlePlayer = player.mcServer.isSinglePlayer();
			boolean isOp = player.mcServer.getOpPermissionLevel() <= 2;
			// TODO implement marquee, then remove comments
			if (chattedBools && criticals && (isOp || singlePlayer) /* && Config.chat*/) {
				ChatComponentText text = new ChatComponentText("There are critical updates available, click the mod name to download the update: ");
				text.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
				for (FetchedUpdateable fetched : updates) {
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
	public void registerUpdateable(IUpdateable updateable) {
		FetchedUpdateable toBe = new FetchedUpdateable(updateable);
		if (toBe.diff > 0) {
			updates.add(toBe);
		}
	}
}
