package com.example.disign.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UriToMultipartPartConverter {
    public static MultipartBody.Part convertToMultipartPart(Context context, Uri uri, String partName) {
        ContentResolver contentResolver = context.getContentResolver();
        String mimeType = getMimeType(context, uri);
        RequestBody requestBody = RequestBody.create(getMediaType(mimeType), readBytesFromUri(contentResolver, uri));
        return MultipartBody.Part.createFormData(partName, getFileName(context, uri), requestBody);
    }

    private static byte[] readBytesFromUri(ContentResolver contentResolver, Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getMimeType(Context context, Uri uri) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    private static MediaType getMediaType(String mimeType) {
        if (mimeType == null) {
            // Handle the case when mimeType is null (provide a default or throw an exception).
            return MediaType.parse("application/octet-stream");
        } else {
            return MediaType.parse(mimeType);
        }
    }

    private static String getFileName(Context context, Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            String[] projection = {android.provider.MediaStore.MediaColumns.DISPLAY_NAME};
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }
}
