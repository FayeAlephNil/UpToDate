package uptodate.java;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import uptodate.java.api.IUpdateable;
import uptodate.java.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = UpToDate.MODID, version = UpToDate.VERSION)
public class UpToDate implements IUpdateable
{
    public static final String MODID = "uptodate";
    public static final String VERSION = "1.0";
	public static final int SIMPLE_VERSION = 1;

	public ArrayList<IUpdateable> low = new ArrayList<IUpdateable>();
	public ArrayList<IUpdateable> severe = new ArrayList<IUpdateable>();
	public ArrayList<IUpdateable> critical = new ArrayList<IUpdateable>();

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		Object objMods = ReflectionUtil.getFieldValFromObj(Loader.instance(), "mods");
		Object objController = ReflectionUtil.getFieldValFromObj(Loader.instance(), "modController");

		if (objController instanceof LoadController && objMods instanceof List) {
			List mods = (List) objMods;
			LoadController controller = (LoadController) objController;

			for (Object mod : mods) {
				if (mod instanceof ModContainer && controller.getModState((ModContainer) mod) != LoaderState.ModState.DISABLED) {
					handleMod((ModContainer) mod);
				}
			}
		}
    }

	@Override
	public String getRemote() {
		return null;
	}

	@Override
	public HashMap<String, String> getLocal() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("technical", String.valueOf(SIMPLE_VERSION));
		result.put("display", VERSION);
		return result;
	}

	public void handleMod(ModContainer mod) {
		
	}
}
