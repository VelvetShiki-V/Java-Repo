package com.vs.blogsystem_v1;

//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.common.auth.*;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.PutObjectRequest;
//import com.aliyun.oss.model.PutObjectResult;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import org.junit.jupiter.api.Test;
//import io.jsonwebtoken.*;
//import com.vs.blogsystem_v1.controller.UserController;
import com.vs.blogsystem_v1.mapper.PhotoMapper;
//import com.vs.pojo.Photo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;

//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

@SpringBootTest
class BlogSystemV1ApplicationTests {
/*    @Autowired
    private ApplicationContext applicationContext;
     获取bean
    @Test
    public void getBean() {
        UserController bean1 = (UserController) applicationContext.getBean("userController");
        System.out.println("获取到bean对象： \n" + bean1);

        UserController bean2 = (UserController) applicationContext.getBean(UserController.class);
        System.out.println("获取到bean对象： \n" + bean2);

        UserController bean3 = (UserController) applicationContext.getBean("userController", UserController.class);
        System.out.println("获取到bean对象： \n" + bean3);

    }
     生成JWT
    @Test
    public String jwtGen() {
        // signWith设置签名算法和秘钥，base64编码
        // setClaims设置payload
        // setExpiration设置token有效期，毫秒为单位
        // compact为打包令牌生成String
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);
        payload.put("username", "shiki");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "Velvet")
                .setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();
        System.out.println("jwt gen: " + jwt);
        return jwt;
    }

    // 解析JWT
    @Test
    public void jwtParse() {
        String privateKey = "Velvet123";
        String jwt = jwtGen();
        // 解析
        // setSignKey设置解析JWT的私钥
        // parseClaimsJws接收JWT
        // getBody解析出Claims类型的payload对象
        Claims payload = null;
        try {
            payload = Jwts.parser()
                    .setSigningKey(privateKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            // 如果JWT解析私钥或JWT被篡改，则解析失败抛异常
            // 签名异常SignatureException， 篡改异常MalformedJwtException, token过期异常ExpiredException...）
            e.printStackTrace();
        }
        System.out.println("获取到解析payload: " + payload);
        // jwt gen: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzE5NDU5MjU3LCJ1c2VybmFtZSI6InNoaWtpIn0.ekJ1cf2DyaBsAVZ6Bj9-oIKEHkmHshoM5zRe1a8bV3M
        // 获取到解析payload: {id=1, exp=1719459257, username=shiki}
    }

    @Test
    void contextLoads() {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        // 临时访问凭证
        // STS临时访问密钥AccessKey ID和AccessKey Secret。
        String accessKeyId = "LTAI5tRsRWHGiTAUm9ppDCDc";
        String accessKeySecret = "ywbq1XsBWLQF3TUZ1YrkWvwxmaSscv";

        // STS安全令牌SecurityToken。
//        String securityToken = "yourSecurityToken";

        // 使用代码嵌入的STS临时访问密钥和安全令牌配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "vsb-image";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "test/6a26c2631d964343b40eedeb6850993c.jpg";
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "D:\\Code\\java-repo\\BlogSystem_v1\\src\\main\\resources\\static\\6a26c2631d964343b40eedeb6850993c.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        System.out.println("OSS 创建成功");

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException | FileNotFoundException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }*/

    @Autowired
    private PhotoMapper photoMapper;

    @Test
    public void test() {
//        查询全部
//        List<Photo> list = photoMapper.selectList(null);
//        for(Photo p: list) {
//            System.out.println(p);
//        }

//        // 插入
//        Photo photo = new Photo(null, 1, "shiki", "www.nice.com", LocalDateTime.now());
//        photoMapper.insert(photo);

        // 更新
//        Photo photo = new Photo(5, 2, "velvet", "www.niceeeee.com", LocalDateTime.now());
//        photoMapper.updateById(photo);

        // 删除
//        photoMapper.deleteById(5);
    }

}
