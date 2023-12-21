/*
 *
 */

package me.leoo.bedwars.mapselector.utils;

import lombok.Getter;

@Getter
public enum BedwarsMode {

	BEDWARS("BedWars1058"),
	PROXY("BedWarsProxy");

	private final String name;

	BedwarsMode(String name){
		this.name = name;
	}

}
