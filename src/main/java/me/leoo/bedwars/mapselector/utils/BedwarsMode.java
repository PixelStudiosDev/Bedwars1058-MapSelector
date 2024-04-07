package me.leoo.bedwars.mapselector.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BedwarsMode {
    BEDWARS("BedWars1058"), PROXY("BedWarsProxy");

    private final String name;

    public String getPath() {
        return "plugins/" + name + "/Addons/MapSelector";
    }
}
