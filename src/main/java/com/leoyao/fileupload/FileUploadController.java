package com.leoyao.fileupload;

import com.alibaba.fastjson.JSON;
import com.alipay.api.FileItem;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FileUploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd") ;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile[] fileList, HttpServletRequest req) throws IOException {
        List<FileItem> fileItemList = Arrays.stream(fileList).map(file -> {
            String name = file.getOriginalFilename();
            System.out.println(name);
//            String newName = UUID.randomUUID().toString() + ".jpg";
            FileItem fileItem = null;
            try {
                fileItem = new FileItem(multipartFileToFile(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println(fileItem.getMimeType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return fileItem;
        }).collect(Collectors.toList());
        System.out.println(fileItemList);
        Map<String, Object> result = new HashMap<>() ;
        result.put("status", "success") ;
        result.put("fileList", fileItemList) ;
        /*MultipartFile file = files[0];
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.endsWith(".jpeg")) {
            result.put("status", "error") ;
            result.put("message", "文件类型不匹配，应该上传jpeg格式图片") ;
            return result ;
        }
        String format = sdf.format(new Date());
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static" + format ;
        System.out.println(realPath);
        //D:\java_workspace\project-0726\fileupload\target\classes\static\2022\11\03
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs() ;
        }
        String newName = UUID.randomUUID().toString() + ".jpeg";
        FileItem fileItem = new FileItem(newName,file.getBytes());
        System.out.println(fileItem);
        try {
            file.transferTo(new File(folder, newName));
//            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName ;
            String url = "file://" + realPath + "/" + newName ;
            System.out.println(url);
            result.put("status", "success") ;
            result.put("url", url) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        return result ;
    }

//    @PostMapping("/upload")
//    public Map<String, Object> upload(@RequestParam("file") MultipartFile[] file) throws IOException {
//
//        Map<String, Object> result = new HashMap<>() ;
//        result.put("status", "success") ;
//        result.put("fileList", file) ;
//
//        return result ;
//    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
