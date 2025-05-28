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
        String folderPath = request.getFolderPath();

        if(folderPath.isEmpty()) {
            throw new BusinessException("Folder path is empty");
        }

        byte[] bytes = request.getFile().toByteArray();

        Map paramsWithPublicId = ObjectUtils.asMap(
                "resource_type", "auto",
                "public_id", request.getFileId(),
                "asset_folder", folderPath,
                "overwrite", "true"
        );

        Map paramsWithoutPublicId = ObjectUtils.asMap(
                "resource_type", "auto",
                "asset_folder", folderPath,
                "overwrite", "true"
        );

        try {
            Map result = request.getFileId().isEmpty() ?
                    cloudinary.uploader().upload(bytes, paramsWithoutPublicId) :
                    cloudinary.uploader().upload(bytes, paramsWithPublicId);

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
        try {
            String publicId = extractPublicIdFromUrl(request.getUrl());
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "auto"));
        }
        catch (IOException e) {
            throw new BusinessException("Error while deleting file.");
        }
    }

    private String extractPublicIdFromUrl(String url) {
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
