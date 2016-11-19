package com.nos.ploy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by User on 13/11/2559.
 */
public class DrawerController {


    public static final int MENU_COUNT_PLOYEE = 5;
    public static List<String> MAP_MENU_NAMES = new ArrayList<>();

    static{
        MAP_MENU_NAMES.add(0,"Settings");
        MAP_MENU_NAMES.add(1,"Account");
        MAP_MENU_NAMES.add(2,"What is Ployer");
        MAP_MENU_NAMES.add(3,"What is Ployee");
    }
    private static DrawerController INTSTANCE = new DrawerController();

    public static DrawerController getInstance() {
        return INTSTANCE;
    }

    private DrawerController() {
    }


}
