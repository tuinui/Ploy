package com.nos.ploy.utils;

import android.content.Context;

import com.nos.ploy.R;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import rx.functions.Action1;

/**
 * Created by Saran on 18/12/2559.
 */

public class ImagePickerUtils {


    public static void pickImage(Context context, Picker.PickListener listener) {
        pickImage(context, 1, listener);
    }

    public static void pickImage(Context context, final int limit, final Picker.PickListener listener) {
        if (null == context) {
            return;
        }
        new Picker.Builder(context, new Picker.PickListener() {
            @Override
            public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
                if (null != images && !images.isEmpty() && null != listener) {
                    listener.onPickedSuccessfully(images);
                }
            }

            @Override
            public void onCancel() {
                if (null != listener) {
                    listener.onCancel();
                }

            }
        }, R.style.AppTheme)
                .setLimit(limit)
                .build()
                .startActivity();
    }

    public static void pickImage(Context context, final Action1<ImageEntry> onConfirm) {
        if (null == context) {
            return;
        }
        new Picker.Builder(context, new Picker.PickListener() {
            @Override
            public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
                if (null != images && !images.isEmpty() && null != onConfirm) {
                    onConfirm.call(images.get(0));
                }
            }

            @Override
            public void onCancel() {

            }
        }, R.style.AppTheme)
                .setLimit(1)
                .build()
                .startActivity();
    }

//    public static MultipartBody.Part intoMultipartBodyFile(String key, ImageEntry image) {
//        File file = new File(image.path);
//        if (file.exists()) {
//            String mimeType = MyFileUtils.getMimeType(file.getPath());
//            if (null != mimeType) {
//                return MultipartBody.Part.createFormData(key, file.getName(), RequestBody.create(MediaType.parse(MyFileUtils.getMimeType(file.getPath())), file));
//            }
//        }
//        return null;
//    }

}
