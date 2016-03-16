package com.theironyard.controllers;

import com.theironyard.entities.AnonFile;
import com.theironyard.services.AnonFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by branden on 3/16/16 at 11:20.
 */
@RestController
public class AnonUploadController {

    @Autowired
    AnonFileRepository anonFileRepository;

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public void upload(MultipartFile file, HttpServletResponse response) throws IOException {
        File dir = new File("public/files"); //this is a file path
        dir.mkdirs(); //this makes the directory exist for sure. for sure.
        File f = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());

        AnonFile anonFile = new AnonFile(f.getName(), file.getOriginalFilename());
        anonFileRepository.save(anonFile);

        response.sendRedirect("/");
    }

    @RequestMapping(path = "/upload", method = RequestMethod.GET)
    public List<AnonFile> showUpload() {

        return (List<AnonFile>) anonFileRepository.findAll();
    }


}