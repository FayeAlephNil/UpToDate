package pt.uptodate;

import java.util.ArrayList;

/**
 * @author Strikingwolf
 */
public class FetchedList extends ArrayList<FetchedUpdateable> {
	public ArrayList<FetchedUpdateable> normal = new ArrayList<FetchedUpdateable>();
	public ArrayList<FetchedUpdateable> severe = new ArrayList<FetchedUpdateable>();
	public ArrayList<FetchedUpdateable> critical = new ArrayList<FetchedUpdateable>();

	@Override
	public boolean add(FetchedUpdateable fetched) {
		if (fetched.severity >= 3) {
			critical.add(fetched);
		} else if (fetched.severity >= 2) {
			severe.add(fetched);
		} else {
			normal.add(fetched);
		}
		return super.add(fetched);
	}
}
