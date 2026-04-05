/**
 * 
 */
package br.com.meslin.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * @author meslin
 *
 */
public class Inspector implements MobileObject {
	private Date date;
	private double latitude;
	private double longitude;
	private HashSet<Integer> groups;
	private UUID uuid;

	/**
	 * Constructor<br>
	 */
	public Inspector() {
		this.groups = new HashSet<Integer>();
	}

	/**
	 * Constructor<br>
	 * @param date last seen date
	 * @param latitude last seen latitude
	 * @param longitude last seen longitude
	 * @param groups inspector groups
	 * @param uuid inspector UUID
	 */
	public Inspector(Date date, double latitude, double longitude, HashSet<Integer> groups, UUID uuid) {
		super();
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.groups = groups;
		this.uuid = uuid;
	}
	
	public Inspector(Entry<String, Inspector> inspector) {
		this.date = inspector.getValue().getDate();
		this.latitude = inspector.getValue().getLatitude();
		this.longitude = inspector.getValue().getLongitude();
		this.groups = inspector.getValue().getGroups();
		this.uuid = inspector.getValue().getUuid();
	}

	/**
	 * Constructor
	 * 
	 * @param date Mon Jun 28 16:00:14 BRT 2021
	 * @param latitude in degrees
	 * @param longitude in degrees
	 * @param uuid sender UUID
	 * @throws ParseException if date not in "E MMM dd HH:mm:ss z yyyy" English format
	 */
	public Inspector(String date, double latitude, double longitude, String uuid) throws ParseException {
		date = date.substring(1, date.length()-1);
		uuid = uuid.substring(1, date.length()-1);
		this.date = (new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)).parse("Mon Jun 28 18:04:22 BRT 2021");
		this.latitude = latitude;
		this.longitude = longitude;
		this.uuid = UUID.fromString(uuid);
	}
	
	/**
	 * Constructor
	 * 
	 * @param date Mon Jun 28 16:00:14 BRT 2021
	 * @param latitude in degrees
	 * @param longitude in degrees
	 * @param uuid sender UUID
	 */
	public Inspector(Date date, double latitude, double longitude, String uuid) {
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.uuid = UUID.fromString(uuid);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HashSet<Integer> getGroups() {
		return groups;
	}

	public void setGroups(HashSet<Integer> groups) {
		this.groups = groups;
	}
	public void addGroup(int group) {
		if(this.groups==null) {
			this.groups = new HashSet<Integer>();
		}
		this.groups.add(group);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
