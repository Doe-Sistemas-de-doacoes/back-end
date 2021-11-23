package com.labes.doe.service.impl;

import com.labes.doe.exception.BusinessException;
import com.labes.doe.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    private final S3AsyncClient s3AsyncClient;

    @Override
    public Mono<String> uploadF(Mono<FilePart> file) {
        return file.flatMap(filePart -> {
            var filename = UUID.randomUUID().toString().concat("_").concat(filePart.filename());
            return filePart
                    .content()
                    .flatMap(dataBuffer -> Mono.fromFuture(sendToS3( filename, dataBuffer)))
                    .filter(putObjectResponse -> putObjectResponse.sdkHttpResponse().isSuccessful())
                    .switchIfEmpty(Mono.error(new BusinessException("Não foi possível realizar o upload.")))
                    .then( Mono.just( String.format( "https://s3.amazonaws.com/%s/%s", bucketName, filename ) ) );
        });
    }

    private CompletableFuture<PutObjectResponse> sendToS3(String filename, DataBuffer dataBuffer){
        return s3AsyncClient
                .putObject(PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(filename)
                        .build(),
                AsyncRequestBody.fromByteBuffer(dataBuffer.asByteBuffer()));
    }

}
