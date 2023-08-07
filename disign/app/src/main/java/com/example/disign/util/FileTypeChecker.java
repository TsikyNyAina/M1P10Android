package com.example.disign.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class FileTypeChecker {

    public static boolean isVideo(Context context, Uri uri) {
        String mimeType=getMimeType(context,uri);
        return mimeType != null && mimeType.startsWith("video/");
    }
    public static String getMimeType(Context context,Uri uri){
        String mimeType =null;


        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver contentResolver = context.getContentResolver();
            mimeType = contentResolver.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    public static boolean isImage(Context context, Uri uri) {
        String mimeType=getMimeType(context,uri);
        return mimeType != null && mimeType.startsWith("image/");
    }
}
