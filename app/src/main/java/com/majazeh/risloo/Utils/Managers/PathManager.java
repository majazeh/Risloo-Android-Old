package com.majazeh.risloo.Utils.Managers;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.util.Objects;

public class PathManager {

    public String getLocalPath(Context context, Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // Local
            if (isLocalStorageDocument(uri)) {
                return DocumentsContract.getDocumentId(uri);
            }

            // External
            else if (isExternalStorageDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                String[] split = documentId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                }
            }

            // Downloads
            else if (isDownloadsDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);

                if (documentId != null && documentId.startsWith("raw:")) {
                    return documentId.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[] {
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    String[] split = documentId.split(":");

                    Uri contentUri;
                    if (Objects.requireNonNull(documentId).startsWith("msf:")) {
                        contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(split[1]));
                    } else {
                        contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(documentId));
                    }

                    String path = getFileColumn(context, contentUri, null, null);
                    if (path != null) {
                        return path;
                    }
                }

                String fileName = getFileName(context, uri);
                if (fileName != null) {
                    File file = new File(FileManager.readFileFromCache(context, "documents"), fileName);
                    String path = file.getAbsolutePath();
                    FileManager.writeUriToCache(context, uri, path);
                    return path;
                }
            }

            // Media
            else if (isMediaDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                String[] split = documentId.split(":");
                String type = split[0];

                Uri contentUri;
                if (type.equals("image")) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("video")) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("audio")) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    contentUri = null;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[] {split[1]};

                return getFileColumn(context, contentUri, selection, selectionArgs);
            }

        }

        // MediaStore
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            } else {
                return getFileColumn(context, uri, null, null);
            }
        }

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getFileName(Context context, Uri uri) {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                return cursor.getString(nameIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private String getFileColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;

        String column = MediaStore.Files.FileColumns.DATA;
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private boolean isLocalStorageDocument(Uri uri) {
        return "com.majazeh.risloo.debug.fileprovider".equals(uri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}