package pt.uptodate;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import pt.api.IUpdateable;
import pt.uptodate.util.Logger;
import pt.uptodate.util.ReflectionUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = UpToDate.MOD_ID, version = UpToDate.VERSION, name = UpToDate.MOD_NAME)
public class UpToDate implements IUpdateable
{
    public static final String MOD_ID = "uptodate";
	public static final String MOD_NAME = "UpToDate";
    public static final String VERSION = "1.0";
	public static final int SIMPLE_VERSION = 1;

	public ArrayList<FetchedUpdateable> updates = new ArrayList<FetchedUpdateable>();

	@EventHandler
	public void complete(FMLLoadCompleteEvent event) {
		if (netIsAvailable()) {
			Object objMods = ReflectionUtil.getFieldValFromObj(Loader.instance(), "mods");
			Object objController = ReflectionUtil.getFieldValFromObj(Loader.instance(), "modController");

			if (objController instanceof LoadController && objMods instanceof List) {
				List mods = (List) objMods;
				LoadController controller = (LoadController) objController;
				Logger.info("Got mods list");
				for (Object mod : mods) {
					if (mod instanceof ModContainer && ((ModContainer) mod).getMod() instanceof IUpdateable) {
						IUpdateable modObj = (IUpdateable) ((ModContainer) mod).getMod();
						Logger.info("Checking if " + modObj.getName() + " is out of date");
						FetchedUpdateable toBe = new FetchedUpdateable(modObj);
						if (toBe.diff > 0) {
							updates.add(toBe);
						}
					}
				}
			}

			Logger.info("The following mods are out of date: " + StringUtils.join(updates, ','));
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
			List codedContents = service.getContents(repo, "version.yaml");
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

	private static boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}
}
