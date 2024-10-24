package com.vs._2024_10_24;

import com.vs._2024_10_24.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ApplicationTests {

    private final String IMG_PATH = "D:\\File\\图片\\2\\images\\";
    private final String IMG_SUFFIX = ".jpg";
    private final String BUCKET_NAME = "bucket1";
    private String bucketPolicyJson = "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Sid\": \"PublicRead\",\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\"*\"]\n" +
            "            },\n" +
            "            \"Action\": [\"s3:GetObject\"],\n" +
            "            \"Resource\": [\"arn:aws:s3:::bucket1/*\"]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Autowired
    private MinioClient minioClient;

    // 测试连接
    @Test
    void testConnectMinio() {
        System.out.println(minioClient);
    }

    // api测试
    @Test
    void testApi() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 创建桶
        minioClient.makeBucket(MakeBucketArgs.builder().bucket("bucket1").build());
        // 列出所有桶
        List<Bucket> ls = minioClient.listBuckets();
        ls.stream().forEach(System.out::println);
        // 桶是否存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket1").build());
        System.out.println("bucket1 exist: " + isExist);
        Bucket bucket = ls.get(0);
        System.out.println("bucket name: " + bucket.name());
        System.out.println("create time" + bucket.creationDate());
        // 删除桶
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket("bucket1").build());
    }

    @Test
    void testObject() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 在桶中创建object
        // 方式一：putObject
        if(minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket1").build())) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            File file = new File(IMG_PATH + "1.jpg");
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket("bucket1")
                    .object(uuid + IMG_SUFFIX)
                    // 设置上传的文件路径，文件大小，缓冲区大小
                    .stream(new FileInputStream(file), file.length(), -1)
                    .build());
            System.out.println("上传: " + response);
            // 上传: io.minio.ObjectWriteResponse@3b021664
        }

        // 方式二：uploadObject
        // 同名文件会被覆盖
        if(minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket1").build())) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            ObjectWriteResponse response = minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("bucket1")
                    .object(uuid + IMG_SUFFIX)
                    .filename(IMG_PATH + "2.jpg")
                    .build());
            System.out.println("upload: " + response);
        }

        // 查询文件是否存在
        StatObjectResponse response = minioClient.statObject(StatObjectArgs.builder()
                .bucket("bucket1")
                .object("72a7c7199cc344f4a66c159ba7867b72.jpg")
                .build());
        System.out.println("file stat: " + response);
        // file stat: ObjectStat{bucket=bucket1, object=72a7c7199cc344f4a66c159ba7867b72.jpg,
        // last-modified=2024-10-24T06:32:07Z, size=1684145}
        // 文件不存在会抛异常

        // 获取对象url(获取minio对象签名url)
        String signedurl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("bucket1")
                .object("72a7c7199cc344f4a66c159ba7867b72.jpg")
                .expiry(3, TimeUnit.MINUTES)        // 指定资源过期时间
                .method(Method.GET)     // 使用get请求获取
                .build());
        System.out.println("signed url: " + signedurl);
        // signed url: http://localhost:9000/bucket1/72a7c7199cc344f4a66c159ba7867b72.jpg?X-Amz
        // -Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=shiki%2F20241024%2Fus-east-1%2Fs3%2Faws4
        // _request&X-Amz-Date=20241024T064406Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz
        // -Signature=cf647c146688f307a9060bd704a677911fa50130573b2b50ff4efb11cd7b2760
    }

    @Test
    void testObject2() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 桶权限设置
//        if(minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket1").build())) {
//            // 允许公开读，私有写
//            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
//                    .bucket("bucket1")
//                    .config(bucketPolicyJson)
//                    .build());

        // 下载文件
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket("bucket1")
                .object("aab16089e1cf404188747881388a5448.jpg")
                .build());
        // 包含了文件名，文件大小等详细信息
        System.out.println("download obj: " + response);
        System.out.println("obj name: " + response.object());
        // download obj: io.minio.GetObjectResponse@368424db
        // obj name: aab16089e1cf404188747881388a5448.jpg

        // 列出桶中所有obj文件
        Iterable<Result<Item>> list = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("bucket1")
                .build());
        list.forEach(item -> {
            try {
                System.out.println("traverse item get name: " + item.get().objectName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // traverse item get name: 72a7c7199cc344f4a66c159ba7867b72.jpg
        // traverse item get name: aab16089e1cf404188747881388a5448.jpg
        // traverse item get name: da779adf-2c9b-4c7a-8158-aad8eb5872ab.jpg
        // traverse item get name: fdb6583e77b247dfad65555e776ce4bf.jpg

        // 转储到磁盘
        response.transferTo(new FileOutputStream("D:\\Code\\Github\\Java-Repo" +
                "\\demo\\_2024_10_24\\src\\main\\resources\\1.jpg"));

        // 删除文件
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("bucket1")
                .object("da779adf-2c9b-4c7a-8158-aad8eb5872ab.jpg")
                .build());
    }
}
