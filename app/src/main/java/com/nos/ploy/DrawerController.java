package com.nos.ploy;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.drawer.DrawerRecyclerAdapter;
import com.nos.ploy.utils.RecyclerUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13/11/2559.
 */
public class DrawerController {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, ACCOUNT, SETTINGS, WHAT_IS_PLOYER, WHAT_IS_PLOYEE,INTRODUCTION})
    public @interface Menu {
    }
    public static final int NONE = -404;
    public static final int SETTINGS = 0;
    public static final int ACCOUNT = 1;
    public static final int WHAT_IS_PLOYER = 2;
    public static final int WHAT_IS_PLOYEE = 3;
    public static final int INTRODUCTION = 4;
    public static List<DrawerMenuItem> PLOYEE_MENUS = new ArrayList<>();
    public static List<DrawerMenuItem> PLOYER_MENUS = new ArrayList<>();
    private static DrawerController INTSTANCE = new DrawerController();
    public static DrawerMenuItem MENU_INTRODUCTION = new DrawerMenuItem(INTRODUCTION, "Introduction");
    public static DrawerMenuItem MENU_SETTINGS = new DrawerMenuItem(SETTINGS, "Settings");
    public static DrawerMenuItem MENU_ACCOUNT = new DrawerMenuItem(ACCOUNT, "Account");
    static {

//        DrawerMenuItem whatIsPloyer = new DrawerMenuItem(WHAT_IS_PLOYER, "What is Ployer");
//        DrawerMenuItem whatIsPloyee = new DrawerMenuItem(WHAT_IS_PLOYEE, "What is Ployee");
        PLOYEE_MENUS.add(MENU_SETTINGS);
        PLOYEE_MENUS.add(MENU_ACCOUNT);
        PLOYEE_MENUS.add(MENU_INTRODUCTION);
//        PLOYEE_MENUS.add(whatIsPloyer);
//        PLOYEE_MENUS.add(whatIsPloyee);

        PLOYER_MENUS.add(MENU_SETTINGS);
        PLOYER_MENUS.add(MENU_ACCOUNT);
        PLOYER_MENUS.add(MENU_INTRODUCTION);
//        PLOYER_MENUS.add(whatIsPloyer);
//        PLOYER_MENUS.add(whatIsPloyee);
    }

    private DrawerController() {
    }

    public static DrawerController getInstance() {
        return INTSTANCE;
    }

    public static void initDrawer(final BaseActivity activity, List<DrawerMenuItem> menus, final DrawerLayout drawerLayout, RecyclerView recyclerViewMenu, Toolbar toolbar, View toggleButton, final OnMenuItemSelectedListener listener) {
        initDrawer(activity, menus, drawerLayout, recyclerViewMenu, toolbar, toggleButton, Color.WHITE, listener);
    }

    public static void initDrawer(final BaseActivity activity, final List<DrawerMenuItem> menus, final DrawerLayout drawerLayout, RecyclerView recyclerViewMenu, Toolbar toolbar, View toggleButton, @ColorInt int textColor, final OnMenuItemSelectedListener listener) {
//        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        DrawerRecyclerAdapter adapter = new DrawerRecyclerAdapter(textColor) {
            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                if (RecyclerUtils.isAvailableData(menus, position)) {
                    final DrawerMenuItem menu = menus.get(position);
                    holder.tvItem.setText(menu.getMenuTitle());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (RecyclerUtils.isAvailableData(menus, holder.getAdapterPosition())) {
                                @DrawerController.Menu int menu = menus.get(holder.getAdapterPosition()).getMenu();
                                listener.onMenuItemSelected(menu);
                                closeDrawer(drawerLayout);
                            }
                        }
                    });
                }

            }

            @Override
            public int getItemCount() {
                return RecyclerUtils.getSize(menus);
            }
        };
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerViewMenu.addItemDecoration(new DividerItemDecoration(recyclerViewMenu.getContext(), DividerItemDecoration.VERTICAL));
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawer(drawerLayout);
            }
        });
        recyclerViewMenu.setAdapter(adapter);
    }


//    private
//    @DrawerController.Menu
//    static int toMenu(String menu) {
//        Map<Integer, String> map = DrawerController.PLOYEE_MENUS;
//        for (Integer index : map.keySet()) {
//            String menuString = map.get(index);
//            if (TextUtils.equals(menu, menuString)) {
//                return index;
//            }
//        }
//        return DrawerController.NONE;
//    }

    private static void toggleDrawer(DrawerLayout dl) {
        if (null != dl && null != dl.getContext()) {
            if (dl.isDrawerVisible(GravityCompat.END)) {
                closeDrawer(dl);
            } else {
                openDrawer(dl);
            }
        }
    }

    private static void openDrawer(DrawerLayout dl) {
        if (null != dl && null != dl.getContext()) {
            dl.openDrawer(GravityCompat.END);
        }
    }

    private static void closeDrawer(DrawerLayout dl) {
        if (null != dl && null != dl.getContext()) {
            dl.closeDrawer(GravityCompat.END);
        }
    }


    public static interface OnMenuItemSelectedListener {
        void onMenuItemSelected(@DrawerController.Menu int menu);
    }

    public static class DrawerMenuItem {
        private String menuTitle;
        private
        @DrawerController.Menu
        int menu;

        public DrawerMenuItem(int menu, String menuTitle) {
            this.menu = menu;
            this.menuTitle = menuTitle;
        }

        public @DrawerController.Menu int getMenu() {
            return menu;
        }

        public String getMenuTitle() {
            return menuTitle;
        }

        public void setMenuTitle(String menuTitle){
            this.menuTitle = menuTitle;
        }
    }
}
