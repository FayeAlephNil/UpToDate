package pt.uptodate.java;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import org.yaml.snakeyaml.Yaml;
import pt.uptodate.java.api.IUpdateable;
import pt.uptodate.java.util.ReflectionUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(modid = UpToDate.MODID, version = UpToDate.VERSION)
public class UpToDate implements IUpdateable
{
    public static final String MODID = "uptodate";
	public static final String MODNAME = "UpToDate";
    public static final String VERSION = "1.0";
	public static final int SIMPLE_VERSION = 1;

	public ArrayList<FetchedUpdateable> updates = new ArrayList<FetchedUpdateable>();

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		Object objMods = ReflectionUtil.getFieldValFromObj(Loader.instance(), "mods");
		Object objController = ReflectionUtil.getFieldValFromObj(Loader.instance(), "modController");

		if (objController instanceof LoadController && objMods instanceof List) {
			List mods = (List) objMods;
			LoadController controller = (LoadController) objController;

			for (Object mod : mods) {
				if (mod instanceof IUpdateable && controller.getModState((ModContainer) mod) != LoaderState.ModState.DISABLED) {
					FetchedUpdateable toBe = new FetchedUpdateable((IUpdateable) mod);
					if (toBe.diff > 0) {
						updates.add(toBe);
					}
				}
			}
		}
    }

	@Override
	public String getName() {
		return "UpToDate";
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
}
