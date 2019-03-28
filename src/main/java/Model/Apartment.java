package Model;

public class Apartment {

    private enum Level {LEVEL_0,LEVEL_1,LEVEL_2,LEVEL_3,LEVEL_4,LEVEL_5,LEVEL_6,LEVEL_7,LEVEL_8}

    private Dormitory_Type dormitory_type;
    private Disabled_Access disabled_access;
    private Room_Quantity room_quantity;
    private Level level;
    private int price;

}
