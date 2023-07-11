package com.regi.web;

import com.regi.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class Commoncontroller {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() +suffix;
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void dowmload(String name, HttpServletResponse response){
        try {
            FileInputStream fio = new FileInputStream(new File(basePath+name));
            ServletOutputStream ops = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[]bytes = new byte[1024];
            while ((len=fio.read(bytes))!=-1){
                ops.write(bytes,0,len);
                ops.flush();
            }
            fio.close();
            ops.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
