package poo.group4;

import java.io.Serializable;

/**
 * A single attribute of the BNC. It is identified by its {@link #index} field.
 * 
 * @author Group 4
 */
public class Attribute implements Serializable, Comparable<Attribute> {

	/**  */
	private static final long serialVersionUID = 1L;

	/** Unique index of the attribute in the Train Data. */
	int index;

	/** Friendly name of the attribute. */
	String name;

	/** Number of possible values for this attribute. The values go from 0 to (range-1). */
	private int range;

	/**
	 * Creates a new attribute, with a given name and index. Its range is 0 by default, and should be updated as needed.
	 * 
	 * @param name
	 *            Friendly name of the attribute.
	 * @param index
	 *            Index of the attribute, in the Train Data file.
	 */
	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (index != other.index)
			return false;
		return true;
	}

	/**
	 * Gets the number of possible values for this attribute. The values go from 0 to (range-1).
	 * 
	 * @return Range of the attribute.
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Sets the number of possible values for this attribute.
	 * 
	 * @param range
	 *            New range of the attribute.
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * Gets the friendly name of the attribute.
	 * 
	 * @return The friendly name of the attribute.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Compares this attribute with another attribute object, using their {@link #index} fields.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Attribute o) {
		return this.index - o.index;
	}

}
