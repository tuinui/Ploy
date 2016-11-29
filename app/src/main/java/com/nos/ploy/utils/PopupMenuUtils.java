package com.nos.ploy.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Saran on 19/11/2559.
 */

public class PopupMenuUtils {


    public static class MenuVM<T> {
        private String menuTitle;
        private Action1<T> action;
        private T data;

        public MenuVM(String menuTitle, T data, Action1<T> action) {
            this.menuTitle = menuTitle;
            this.action = action;
            this.data = data;
        }


        public String getMenuTitle() {
            return menuTitle;
        }

        public Action1<T> getAction() {
            return action;
        }

        public T getData() {
            return data;
        }
    }

    public static void showConfirmationAlertMenu(final Context context, @StringRes Integer titleResource, @StringRes Integer messageResource, final Action1<Boolean> onConfirm) {
        if (null == context || null == context.getResources()) {
            return;
        }
        String title = null, message = null;
        if (null != titleResource) {
            title = context.getString(titleResource);
        }
        if (null != messageResource) {
            message = context.getString(messageResource);
        }

        showConfirmationAlertMenu(context, title, message, onConfirm);
    }

    public static void showConfirmationAlertMenu(final Context context, @StringRes Integer titleResource, @StringRes Integer messageResource, @StringRes Integer positiveButtonTextResource, @StringRes Integer negativeButtonTextResource, final Action1<Boolean> onConfirm) {
        if (null == context || null == context.getResources()) {
            return;
        }
        String title = null, message = null;
        if (null != titleResource) {
            title = context.getString(titleResource);
        }
        if (null != messageResource) {
            message = context.getString(messageResource);
        }

        showConfirmationAlertMenu(context, title, message, positiveButtonTextResource, negativeButtonTextResource, onConfirm);
    }


    public static void showConfirmationAlertMenu(final Context context, CharSequence title, CharSequence message, final Action1<Boolean> onConfirm) {
        showConfirmationAlertMenu(context, title, message, android.R.string.ok, android.R.string.cancel, onConfirm);
    }

    public static void showConfirmationAlertMenu(final Context context, CharSequence title, CharSequence message, @StringRes Integer positive, @StringRes Integer negative, final Action1<Boolean> onConfirm) {
        showConfirmationAlertMenu(context, title, message, context.getString(positive), context.getString(negative), onConfirm);
    }

    public static void showConfirmationAlertMenu(final Context context, CharSequence title, CharSequence message, String positive, String negative, final Action1<Boolean> onConfirm) {
        if (null == context) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(Html.fromHtml("<b>" + title + "<b>"));
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != onConfirm) {
                    onConfirm.call(true);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != onConfirm) {
                    onConfirm.call(false);
                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        if (null != dialog) {
            dialog.show();
        }
    }


    public static void showDialogMenu(String title, final List<MenuVM> datas, final Context context) {
        if (null == context) {
            return;
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        final ArrayAdapter<MenuVM> arrayAdapter = new ArrayAdapter<MenuVM>(
                context,
                android.R.layout.simple_list_item_1, datas) {
            @NonNull
            @Override
            public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                MenuVM vm = getItem(position);
                if (null != vm) {
                    tv.setText(vm.getMenuTitle());
                }

                return tv;
            }
        };

        if (!TextUtils.isEmpty(title)) {
            builderSingle.setTitle(title);
        }


        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MenuVM data = datas.get(which);
                        if (null != data && null != data.getAction()) {
                            data.getAction().call(data.getData());
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builderSingle.create();
        if (null != dialog) {
            dialog.show();
        }
    }

    public static void showDialogMenu(final List<MenuVM> datas, final Context context) {
        showDialogMenu(null, datas, context);
    }


    /**
     * now support inputType.length = 3;
     *
     * @param context
     * @param title
     * @param onConfirm
     * @param inputTypes
     * @return
     */
    public static void showPopupAlertEditTextMenu(Context context, CharSequence title, CharSequence defaultValue, final Action1<String> onConfirm, int... inputTypes) {
        if (null == context) {
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_edittext_in_alert_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.edittext_in_alert_dialog);
        if (inputTypes.length == 1) {
            editText.setInputType(inputTypes[0]);
        } else if (inputTypes.length == 2) {
            editText.setInputType(inputTypes[0] | inputTypes[1]);
        } else if (inputTypes.length >= 3) {
            editText.setInputType(inputTypes[0] | inputTypes[1] | inputTypes[2]);
        }
        editText.setText(defaultValue);

        alert.setView(view);
        alert.setTitle(title);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onConfirm.call(editText.getText().toString());
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        if (null != dialog) {
            dialog.show();
        }
    }


    public static void showPopupAlertEditTextFromEditTextMenu(EditText editText, final Action1<String> onConfirm, int... inputTypes) {
        if (null == editText) {
            return;
        }
        showPopupAlertEditTextMenu(editText.getContext(), editText.getHint(), editText.getText(), onConfirm, inputTypes);
    }

    public static void showPopupAlertEditTextFromEditTextMenu(EditText editText, final Action1<String> onConfirm) {
        if (null == editText) {
            return;
        }
        CharSequence title = "";
        if(TextUtils.isEmpty(editText.getHint()) && editText instanceof MaterialEditText){
            title = ((MaterialEditText) editText).getFloatingLabelText();
        }else{
            title = editText.getHint();
        }
        showPopupAlertEditTextMenu(editText.getContext(), title, editText.getText(), onConfirm, editText.getInputType());
    }


    public static void showPopupListMenus(final View anchor, final List<MenuVM> infos) {
        if (null == anchor || !isAvaiableContext(anchor.getContext())) {
            return;
        }

        final ListPopupWindow popupWindow = new ListPopupWindow(anchor.getContext());
        popupWindow.setModal(true);
        popupWindow.setAnchorView(anchor);
        popupWindow.setForceIgnoreOutsideTouch(false);
        ArrayAdapter<MenuVM> adapter = new ArrayAdapter<MenuVM>(anchor.getContext(), android.R.layout.simple_list_item_1, infos) {
            @NonNull
            @Override
            public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

                TextView view = (TextView) super.getView(position, convertView, parent);
                final MenuVM data = getItem(position);
                if (data != null) {
                    view.setText(data.getMenuTitle());
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuVM data = infos.get(position);
                        data.getAction().call(data.getData());
                        popupWindow.dismiss();
                    }
                });
                return view;
            }
        };


        popupWindow.setContentWidth(measureContentWidth(adapter, anchor));
        popupWindow.setAdapter(adapter);
        popupWindow.show();
    }


    public interface PopupMenuGetViewListener {
        View onGetView(final int position, View convertView, @NonNull ViewGroup parent, ListPopupWindow popupWindow);
    }

    public static ListPopupWindow showPopupListMenus(final View anchor, final List<? extends MenuVM> infos, @LayoutRes int layoutId, @IdRes int textViewResId, final PopupMenuGetViewListener listener) {
        final ListPopupWindow popupWindow = new ListPopupWindow(anchor.getContext());
        popupWindow.setModal(true);
        popupWindow.setAnchorView(anchor);
        popupWindow.setForceIgnoreOutsideTouch(false);
        ArrayAdapter<? extends MenuVM> adapter = new ArrayAdapter(anchor.getContext(), layoutId, textViewResId, infos) {
            @NonNull
            @Override
            public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
                return listener.onGetView(position, super.getView(position, convertView, parent), parent, popupWindow);
            }
        };


        popupWindow.setContentWidth(measureContentWidth(adapter, anchor));
        popupWindow.setAdapter(adapter);
        return popupWindow;
    }

    private static boolean isAvaiableContext(Context context) {
        if (null == context) {
            return false;
        }

        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            if (activity.isFinishing() || activity.isActivityDestroyedCompat()) {
                return false;
            }
        }
        return true;
    }

//    public static void showInfoDialogAutoLink(Context context, String title, Spanned messageWithLink, String positiveTitle) {
//        if (!isAvaiableContext(context)) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        if (!TextUtils.isEmpty(title)) {
//            builder.setTitle(Html.fromHtml("<b>" + title + "<b>"));
//        }
//
//        if (!TextUtils.isEmpty(messageWithLink)) {
//            FrameLayout root = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog_message_textview, null);
//            TextView textView = (TextView) root.findViewById(R.id.textview_dialog_message);
//            textView.setText(messageWithLink);
//            textView.setMovementMethod(LinkMovementMethod.getInstance());
//            textView.setClickable(true);
//            textView.setAutoLinkMask(Linkify.ALL);
//            textView.setLinksClickable(true);
//
//            builder.setView(root);
//        }
//
//        builder.setIcon(R.drawable.ic_info_gray_24dp);
//        builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        Dialog dialog = builder.create();
//        if (dialog != null) {
//            dialog.show();
//        }
//    }

//    public static void showInfoDialog(Context context, String title, String info, String positiveTitle) {
//        if (!isAvaiableContext(context)) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        if (!TextUtils.isEmpty(title)) {
//            builder.setTitle(Html.fromHtml("<b>" + title + "<b>"));
//        }
//
//        if (!TextUtils.isEmpty(info)) {
//            builder.setMessage(info);
//        }
//
//        builder.setIcon(R.drawable.ic_info_gray_24dp);
//
//        builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        Dialog dialog = builder.create();
//        if (dialog != null) {
//            dialog.show();
//        }
//    }

//    public static void showDoneDialog(Context context, String title, String info, String positiveTitle) {
//        if (!isAvaiableContext(context)) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        if (!TextUtils.isEmpty(title)) {
//            builder.setTitle(Html.fromHtml("<b>" + title + "<b>"));
//        }
//
//        if (!TextUtils.isEmpty(info)) {
//            builder.setMessage(info);
//        }
//
//        builder.setIcon(R.drawable.ic_done_black_24dp);
//
//        builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        Dialog dialog = builder.create();
//        if (dialog != null) {
//            dialog.show();
//        }
//    }

//    public static void showDoneDialog(Activity activity, @StringRes int title, @StringRes int info, @StringRes int positiveTitle) {
//        showDoneDialog(activity, activity.getString(title), activity.getString(info), activity.getString(positiveTitle));
//    }


    private static int measureContentWidth(ListAdapter listAdapter, View view) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = listAdapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(view.getContext());
            }

            itemView = listAdapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }

        return maxWidth + ScreenUtils.convertDIPToPixels(view.getContext(), 16);
    }


    public static void setMenuTitle(Menu menu, @IdRes int menuItemId, String title) {
        MenuItem menuItem = getMenuItemById(menu, menuItemId);
        if (null != menuItem) {
            menuItem.setTitle(title);
        }
    }

    public static void setMenuIcon(Menu menu, @IdRes int menuItemId, @DrawableRes int drawableId) {
        MenuItem menuItem = getMenuItemById(menu, menuItemId);
        if (null != menuItem) {
            menuItem.setIcon(drawableId);
        }
    }

    public static MenuItem getMenuItemById(Menu menu, @IdRes int menuItemId) {
        if (null == menu) {
            return null;
        }
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SubMenu subMenu = item.getSubMenu();
            if (item.getItemId() == menuItemId) {
                return item;
            } else if (subMenu == null) {
                if (item.getItemId() == menuItemId) {
                    return item;
                }
            } else {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    if (subMenuItem.getItemId() == menuItemId) {
                        return subMenuItem;
                    }
                }
            }
        }
        return null;
    }

    public static void clearAndInflateMenu(Toolbar toolbar, @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
        if (toolbar != null) {
            clearMenu(toolbar);
            toolbar.inflateMenu(menu);
            toolbar.setOnMenuItemClickListener(listener);
        }
    }


    /*
     TextView toolbarTitle = null;
        for (int i = 0; i < toolbar.getChildCount(); ++i) {
            View child = toolbar.getChildAt(i);

            // assuming that the title is the first instance of TextView
            // you can also check if the title string matches
            if (child instanceof TextView) {
                toolbarTitle = (TextView)child;
                break;
            }
        }
     */

    public static void clearMenu(Toolbar toolbar) {
        if (null != toolbar) {
            if (null != toolbar.getMenu()) {
                toolbar.getMenu().clear();
            }
        }
    }
}
