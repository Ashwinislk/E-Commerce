package com.electronicstore.service.Impl;

import com.electronicstore.exception.BadApiRequest;
import com.electronicstore.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        log.info("Entering the dao call for upload image");
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            File folder = new File(path);

            if(!folder.exists()){
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            log.info("Completed the dao call for upload image");
            return fileNameWithExtension;
        }else {
            throw new BadApiRequest("File with this" + extension + "Not allowed.");
        }


    }
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        log.info("Entering the dao call for get image");
        String fullPath = path + File.separator + name;
        InputStream inputStream=new FileInputStream(fullPath);
        log.info("Completed the dao call for get image");
        return inputStream;
    }
}
