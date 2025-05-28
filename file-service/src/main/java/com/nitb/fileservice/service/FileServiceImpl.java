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
                "public_id", request.getPublicId(),
                "asset_folder", folderPath,
                "overwrite", "true"
        );

        Map paramsWithoutPublicId = ObjectUtils.asMap(
                "resource_type", "auto",
                "asset_folder", folderPath,
                "overwrite", "true"
        );

        try {
            Map result = request.getPublicId().isEmpty() ?
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
        try {
            cloudinary.api().update(request.getPublicId(),ObjectUtils.asMap("asset_folder", request.getToFolder()));

            if(!request.getNewPublicId().isEmpty()) {
                cloudinary.uploader().rename(request.getPublicId(), request.getNewPublicId(), ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            throw new BusinessException("Error while moving file.");
        }
    }

    @Override
    public void deleteFile(DeleteFileRequest request) {
        try {
            cloudinary.uploader().destroy(request.getPublicId(), ObjectUtils.emptyMap());
        }
        catch (IOException e) {
            throw new BusinessException("Error while deleting file.");
        }
    }
}
