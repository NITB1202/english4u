package com.nitb.fileservice.service;

import com.nitb.fileservice.grpc.DeleteFileRequest;
import com.nitb.fileservice.grpc.MoveFileRequest;
import com.nitb.fileservice.grpc.UploadFileRequest;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(UploadFileRequest request) {
        return "";
    }

    @Override
    public void moveFile(MoveFileRequest request) {

    }

    @Override
    public void deleteFile(DeleteFileRequest request) {

    }
}
