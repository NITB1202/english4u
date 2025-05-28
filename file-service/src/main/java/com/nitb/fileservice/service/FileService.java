package com.nitb.fileservice.service;

import com.nitb.fileservice.grpc.DeleteFileRequest;
import com.nitb.fileservice.grpc.MoveFileRequest;
import com.nitb.fileservice.grpc.UploadFileRequest;

public interface FileService {
    String uploadFile(UploadFileRequest request);
    void moveFile(MoveFileRequest request);
    void deleteFile(DeleteFileRequest request);
}
