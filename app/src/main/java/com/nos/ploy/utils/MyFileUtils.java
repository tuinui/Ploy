package com.nos.ploy.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Saran on 18/12/2559.
 */

public class MyFileUtils {

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }


    public static String encodeToBase64(Bitmap image){
        return encodeToBase64(image, Bitmap.CompressFormat.JPEG,100);
    }


    public static byte[] readFile(File file) {
        byte[] result = null;
        try {
            FileInputStream fis = new FileInputStream(file);

            result = readAllByte(fis);
            fis.close();
        } catch (IOException ignored) {

        }
        return result;
    }

    public static byte[] readAllByte(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        // StringBuffer stringBuffer = new StringBuffer();
        byte[] buffer = new byte[1000];
        int readCount;
        while ((readCount = inputStream.read(buffer, 0, buffer.length)) > 0) {
            // stringBuffer.append(buffer, 0, readCount);
            bb.write(buffer, 0, readCount);
        }


        byte[] result = bb.toByteArray();
        bb.close();
        return result;

    }

}
