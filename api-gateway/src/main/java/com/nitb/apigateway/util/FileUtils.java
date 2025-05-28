package com.nitb.apigateway.util;

import com.nitb.common.exceptions.BusinessException;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Objects;

public class FileUtils {

    public static boolean isImage(FilePart file) {
        MediaType mediaType = file.headers().getContentType();

        String contentType = mediaType != null
                ? mediaType.toString()
                : "";

        return contentType.startsWith("image/");
    }

    public static boolean isExcelFile(FilePart file) {
        String filename = file.filename().toLowerCase();

        return filename.endsWith(".xlsx") ||
                Objects.equals(file.headers().getContentType(), MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    public static String extractPublicIdFromUrl(String url) {
        //Example: https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg
        try {
            int lastSlashIndex = url.lastIndexOf("/");
            String fileName = url.substring(lastSlashIndex + 1);
            int lastDotIndex = fileName.lastIndexOf(".");

            //If url has file extension, remove it
            if (lastDotIndex != -1) {
                return fileName.substring(0, lastDotIndex);
            }

            return fileName;
        } catch (Exception e) {
            throw new BusinessException("Invalid Cloudinary URL: " + url);
        }
    }
}
