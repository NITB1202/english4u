package com.nitb.apigateway.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.fileservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class FileGrpcClient {
    @GrpcClient("file-service")
    private FileServiceGrpc.FileServiceBlockingStub blockingStub;

    public FileResponse uploadFile(String path, byte[] file) {
        ByteString byteString = ByteString.copyFrom(file);

        UploadFileRequest request = UploadFileRequest.newBuilder()
                .setPath(path)
                .setFile(byteString)
                .build();

        return blockingStub.uploadFile(request);
    }

    public Empty moveFile(String from, String to) {
        MoveFileRequest request = MoveFileRequest.newBuilder()
                .setFrom(from)
                .setTo(to)
                .build();

        return blockingStub.moveFile(request);
    }

    public ActionResponse deleteFile(String url) {
        DeleteFileRequest request = DeleteFileRequest.newBuilder()
                .setUrl(url)
                .build();

        return blockingStub.deleteFile(request);
    }
}
