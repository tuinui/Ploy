package com.nos.ploy;

import android.support.annotation.IntDef;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.drawer.DrawerRecyclerAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by User on 13/11/2559.
 */
public class DrawerController {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, ACCOUNT, SETTINGS, WHAT_IS_PLOYER, WHAT_IS_PLOYEE})
    public @interface Menu {
    }

    public static final int NONE = -404;
    public static final int SETTINGS = 0;
    public static final int ACCOUNT = 1;
    public static final int WHAT_IS_PLOYER = 2;
    public static final int WHAT_IS_PLOYEE = 3;

    public static Map<Integer, String> MAP_MENU_NAMES = new LinkedHashMap<>();

    static {
        MAP_MENU_NAMES.put(SETTINGS, "Settings");
        MAP_MENU_NAMES.put(ACCOUNT, "Account");
        MAP_MENU_NAMES.put(WHAT_IS_PLOYER, "What is Ployer");
        MAP_MENU_NAMES.put(WHAT_IS_PLOYEE, "What is Ployee");
    }

    private static DrawerController INTSTANCE = new DrawerController();

    public static DrawerController getInstance() {
        return INTSTANCE;
    }

    private DrawerController() {
    }

    public static void initDrawer(final BaseActivity activity, final DrawerLayout drawerLayout, RecyclerView recyclerViewMenu, Toolbar toolbar, View toggleButton, final OnMenuItemSelectedListener listener) {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        DrawerRecyclerAdapter adapter = new DrawerRecyclerAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                final String menu = DrawerController.MAP_MENU_NAMES.get(position);
                holder.tvItem.setText(menu);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onMenuItemSelected(toMenu(menu));
                        closeDrawer(drawerLayout);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return DrawerController.MAP_MENU_NAMES.size();
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

    private
    @DrawerController.Menu
    static int toMenu(String menu) {
        Map<Integer, String> map = DrawerController.MAP_MENU_NAMES;
        for (Integer index : map.keySet()) {
            String menuString = map.get(index);
            if (TextUtils.equals(menu, menuString)) {
                return index;
            }
        }
        return DrawerController.NONE;
    }

    private static void toggleDrawer(DrawerLayout dl) {
        if (null != dl) {
            if (dl.isDrawerOpen(GravityCompat.END)) {
                closeDrawer(dl);
            } else {
                openDrawer(dl);
            }
        }
    }

    private static void openDrawer(DrawerLayout dl) {
        if (null != dl) {
            dl.openDrawer(GravityCompat.END);
        }
    }

    private static void closeDrawer(DrawerLayout dl) {
        if (null != dl) {
            dl.closeDrawers();
        }
    }

    public static interface OnMenuItemSelectedListener {
        void onMenuItemSelected(@DrawerController.Menu int menu);
    }
}
