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
import org.apache.commons.lang3.StringUtils;
import pt.uptodate.api.IUpdateable;
import pt.uptodate.api.UpdateableUtils;
import pt.uptodate.gui.GuiUpdates;
import pt.uptodate.handlers.Config;
import pt.uptodate.handlers.GuiHandler;
import pt.uptodate.handlers.MainMenu;
import pt.uptodate.util.Logger;
import pt.uptodate.util.Util;

import java.util.HashMap;
import java.util.HashSet;

@Mod(modid = UpToDate.MOD_ID, version = UpToDate.VERSION, name = UpToDate.MOD_NAME)
public class UpToDate implements IUpdateable
{
	public static final String MOD_ID = "uptodate";
	public static final String MOD_NAME = "UpToDate";
	public static final String VERSION = "1.0b";
	public static final String SIMPLE_VERSION = "2";

	public static final FetchedList updates = new FetchedList();
	protected static final HashSet<EntityPlayer> chatted = new HashSet<EntityPlayer>();

	@EventHandler void pre(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Config());
		if (FMLCommonHandler.instance().getSide().equals(Side.CLIENT))
			MinecraftForge.EVENT_BUS.register(new MainMenu());

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
			String[] fetchedNames = new String[updates.size()];
			for (FetchedUpdateable update : updates)
				fetchedNames[fetchedNames.length - 1] = update.name;

			Logger.info("The following mods are out of date: " + StringUtils.join(fetchedNames, ", "));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP && FMLCommonHandler.instance().getSide().equals(Side.SERVER)) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;

			boolean criticals = !updates.getCritical().isEmpty();
			boolean singlePlayer = player.mcServer.isSinglePlayer();
			boolean isOp = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().canSendCommands(player.getGameProfile());
			if (criticals && (isOp || singlePlayer)) {
				if (Config.chat && chatted.contains(player)) {
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

					chatted.add(player);
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
		boolean noNulls = fetched != null && !Util.anyNulls(fetched.display, fetched.displaySeverity, fetched.oldDisp, fetched.name, fetched.url);
		if (noNulls) {
			if (fetched.old < fetched.version) {
				updates.add(fetched);
			}
		} else {
			ModContainer mod = Loader.instance().activeModContainer();
			String name = mod != null && !mod.getModId().equals(MOD_ID) ? mod.getName() : fetched != null && fetched.name != null ? fetched.name : new Throwable().getStackTrace()[1].getClassName();
			Logger.error(name + " has null values");
		}
	}
}
