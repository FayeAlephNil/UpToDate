package uptodate.java;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import uptodate.java.api.IUpdateable;

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

	public void pre(FMLPreInitializationEvent event) {
		
	}

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
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
