package Model;

import java.util.Set;

public abstract class Dormitory {

    public enum Dormitory_Type {
        DALED_EAST,DALED_WEST,DALED_INTERNATIONAL,DALED_TROMIM_MALES,DALED_TROMIM_FEMALES,DALED_DISABLED_ACCESS,GIMEL_DISABLED_ACCESS,GIMEL_EAST,GIMEL_WEST
    }

    protected Set<Building> buildings;
}
