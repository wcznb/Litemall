package cn.edu.xmu.advertisement.util;

import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImgUploads {


    private static Logger logger = LoggerFactory.getLogger(Common.class);

    /**
     * 保存单个图片并限制大小在远程服务器，直接以multipartFile形式
     *
     * @return
     */

    public static ReturnObject remoteSaveImg(MultipartFile multipartFile,
                                             int size, String ACCESSKEY, String SECRETKEY, String BUCKETNAME, String ENDPOINT) throws IOException {


        //判断是否是图片
        if(!isImg(multipartFile))
            return new ReturnObject(ResponseCode.IMG_FORMAT_ERROR);

        //判断文件大小是否符合要求
        if(multipartFile.getSize()>size*1024*1024){
            return new ReturnObject(ResponseCode.IMG_SIZE_EXCEED);
        }

        String s = null;
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);

            //存入bucket不存在则创建，并设置为只读
            if (!minioClient.bucketExists(BUCKETNAME)) {
                minioClient.makeBucket(BUCKETNAME);
                minioClient.setBucketPolicy(BUCKETNAME, "*.*", PolicyType.READ_ONLY);
            }

            String filename =multipartFile.getOriginalFilename();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 文件存储的目录结构
            String objectName = sdf.format(new Date()) + "/" + filename;
            // 存储文件
            minioClient.putObject(BUCKETNAME, objectName, multipartFile.getInputStream(), multipartFile.getContentType());
            logger.info("文件上传成功!");
            s=ENDPOINT + "/" + BUCKETNAME + "/" + objectName;
        } catch (Exception e) {
            logger.info("上传发生错误: {}！", e.getMessage());
        }
        //返回文件名称
        return new ReturnObject(s);

//        Sardine sardine = SardineFactory.begin(username,password);
//
//        //没有权限创建则抛出IOException
//        if(!sardine.exists(baseUrl)){
//            sardine.createDirectory(baseUrl);
//        }
//
//        InputStream fileInputStream = multipartFile.getInputStream();
//
//        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
//        String fileName = UUID.randomUUID() + suffix;
//
//        sardine.put(baseUrl+fileName,fileInputStream);
//
//        sardine.shutdown();
//        return new ReturnObject(fileName);
    }


    /**
     * 删除远程服务器文件
     *
     */
    public static void deleteRemoteImg(String multipartFile,
                                        String ACCESSKEY, String SECRETKEY, String BUCKETNAME,String ENDPOINT){


        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
            minioClient.removeObject(BUCKETNAME,multipartFile);
        } catch (Exception e) {
           logger.error( "删除失败"+e.getMessage());
        }
        return ;

//        Sardine sardine = SardineFactory.begin(username,password);
//        try{
//            sardine.delete(baseUrl+filename);
//            sardine.shutdown();
//        }
//        catch(IOException e){
//            logger.error("delete fail: "+ filename);
//        }
//        return;
    }


    /**
     * 判断文件是否是图片
     *
     * @param multipartFile 文件
     *
     */
    public static boolean isImg(MultipartFile multipartFile) throws IOException{
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return false;
        }
        return true;
    }

}
