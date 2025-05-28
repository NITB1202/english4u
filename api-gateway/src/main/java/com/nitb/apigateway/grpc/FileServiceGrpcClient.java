package com.nitb.apigateway.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.fileservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class FileServiceGrpcClient {
    @GrpcClient("file-service")
    private FileServiceGrpc.FileServiceBlockingStub blockingStub;

    public FileResponse uploadFile(String folderPath, String publicId, byte[] file) {
        String handledPublicId = publicId != null ? publicId : "";
        ByteString byteString = ByteString.copyFrom(file);

        UploadFileRequest request = UploadFileRequest.newBuilder()
                .setFolderPath(folderPath)
                .setPublicId(handledPublicId)
                .setFile(byteString)
                .build();

        return blockingStub.uploadFile(request);
    }

    public Empty moveFile(String oldPublicId, String newPublicId, String toFolder) {
        String handledPublicId = newPublicId != null ? newPublicId : "";

        MoveFileRequest request = MoveFileRequest.newBuilder()
                .setPublicId(oldPublicId)
                .setNewPublicId(handledPublicId)
                .setToFolder(toFolder)
                .build();

        return blockingStub.moveFile(request);
    }

    public ActionResponse deleteFile(String publicId) {
        DeleteFileRequest request = DeleteFileRequest.newBuilder()
                .setPublicId(publicId)
                .build();

        return blockingStub.deleteFile(request);
    }
}
