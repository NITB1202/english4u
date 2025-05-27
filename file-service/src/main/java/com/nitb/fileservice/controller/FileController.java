package com.nitb.fileservice.controller;

import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.fileservice.grpc.*;
import com.nitb.fileservice.mapper.FileMapper;
import com.nitb.fileservice.service.FileService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class FileController extends FileServiceGrpc.FileServiceImplBase {
    private final FileService fileService;

    @Override
    public void uploadFile(UploadFileRequest request, StreamObserver<FileResponse> responseObserver) {
        String url = fileService.uploadFile(request);
        FileResponse response = FileMapper.toFileResponse(url);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void moveFile(MoveFileRequest request, StreamObserver<Empty> responseObserver) {
        fileService.moveFile(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFile(DeleteFileRequest request, StreamObserver<ActionResponse> responseObserver) {
        fileService.deleteFile(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Successfully deleted file.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
