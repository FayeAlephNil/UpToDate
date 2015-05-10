package pt.uptodate;

import java.util.ArrayList;

/**
 * @author Strikingwolf
 */
public class FetchedList extends ArrayList<FetchedUpdateable> {
	protected ArrayList<FetchedUpdateable> normal = new ArrayList<FetchedUpdateable>();
	protected ArrayList<FetchedUpdateable> severe = new ArrayList<FetchedUpdateable>();
	protected ArrayList<FetchedUpdateable> critical = new ArrayList<FetchedUpdateable>();

	/**
	 * Adds a FetchedUpdateable to the list
	 * @param fetched to add
	 * @return whether or not it was added
	 */
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

	/**
	 * Gets the normal updates
	 * @return normal updates
	 */
	public ArrayList<FetchedUpdateable> getNormal() {
		return normal;
	}

	/**
	 * Gets the severe updates
	 * @return severe updates
	 */
	public ArrayList<FetchedUpdateable> getSevere() {
		return severe;
	}

	/**
	 * Gets the critical updates
	 * @return critical updates
	 */
	public ArrayList<FetchedUpdateable> getCritical() {
		return critical;
	}
}
