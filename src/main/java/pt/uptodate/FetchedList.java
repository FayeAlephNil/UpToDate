package pt.uptodate;

import java.util.ArrayList;

/**
 * @author Strikingwolf
 */
public class FetchedList extends ArrayList<FetchedUpdateable> {
	protected ArrayList<FetchedUpdateable> normal = new ArrayList<FetchedUpdateable>();
	protected ArrayList<FetchedUpdateable> severe = new ArrayList<FetchedUpdateable>();
	protected ArrayList<FetchedUpdateable> critical = new ArrayList<FetchedUpdateable>();

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

	public ArrayList<FetchedUpdateable> getNormal() {
		return normal;
	}

	public ArrayList<FetchedUpdateable> getSevere() {
		return severe;
	}

	public ArrayList<FetchedUpdateable> getCritical() {
		return critical;
	}
}
