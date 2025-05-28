package com.nitb.fileservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.fileservice.grpc.DeleteFileRequest;
import com.nitb.fileservice.grpc.MoveFileRequest;
import com.nitb.fileservice.grpc.UploadFileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(UploadFileRequest request) {
        String path = request.getPath();
        byte[] bytes = request.getFile().toByteArray();

        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "public_id", path,
                "overwrite", true
        );

        try {
            Map result = cloudinary.uploader().upload(bytes, params);
            return result.get("secure_url").toString();
        }
        catch (IOException e) {
            throw new BusinessException("Error while uploading file.");
        }
    }

    @Override
    public void moveFile(MoveFileRequest request) {
        Map params = ObjectUtils.asMap(
                "overwrite", true,
                "resource_type", "auto"
        );

        try {
            cloudinary.uploader().rename(request.getFrom(), request.getTo(), params);
        } catch (IOException e) {
            throw new BusinessException("Error while moving file.");
        }
    }

    @Override
    public void deleteFile(DeleteFileRequest request) {
        String publicId = extractPublicIdFromUrl(request.getUrl());

        Map params = ObjectUtils.asMap(
                "resource_type", "auto"
        );

        try {
            cloudinary.uploader().destroy(publicId, params);
        }
        catch (IOException e) {
            throw new BusinessException("Error while deleting file.");
        }
    }

    private String extractPublicIdFromUrl(String url) {
        //Example: https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg
        try {
            //Get path after "/upload/"
            String pattern = "/upload/";
            int uploadIndex = url.indexOf(pattern) + pattern.length();
            String pathAfterUpload = url.substring(uploadIndex); // v1234567890/sample.jpg

            //Remove version (v1234567890/)
            int firstSlash = pathAfterUpload.indexOf("/");
            String publicIdWithExt = pathAfterUpload.substring(firstSlash + 1); // sample.jpg

            //Remove extension (.jpg)
            int lastDot = publicIdWithExt.lastIndexOf(".");

            //Not have extension
            if(lastDot == -1) {
                return publicIdWithExt;
            }

            //Have extension
            return publicIdWithExt.substring(0, lastDot); // sample
        } catch (Exception e) {
            throw new BusinessException("Invalid Cloudinary URL: " + url);
        }
    }
}
