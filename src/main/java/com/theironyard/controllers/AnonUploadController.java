package com.theironyard.controllers;

import com.theironyard.entities.AnonFile;
import com.theironyard.services.AnonFileRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by branden on 3/16/16 at 11:20.
 */
@RestController
public class AnonUploadController {

    @Autowired
    AnonFileRepository anonFileRepository;

    Server dbui = null;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }

    @PreDestroy
    public void preDestory() {
        dbui.stop();
    }




    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public void upload(MultipartFile file, boolean isPermanent, String comment, HttpServletResponse response) throws IOException {

        File dir = new File("public/files"); //this is a file path
        dir.mkdirs(); //this makes the directory exist for sure. for sure.
        File f = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());

        AnonFile anonFile = new AnonFile(f.getName(), file.getOriginalFilename(), isPermanent, comment);



        if (anonFile.getPermanent()) {  //if the file is marked perm add it
            anonFileRepository.save(anonFile);
        } else if (anonFileRepository.findByIsPermanentFalse().size() <= 10){  //other wise get all files not marked perm. less than 10 add it
            anonFileRepository.save(anonFile);
        } else {
            anonFileRepository.delete(anonFileRepository.findFirstByIsPermanentFalseOrderByCreatedAsc());
            anonFileRepository.save(anonFile);
        }


        response.sendRedirect("/");
    }

    @RequestMapping(path = "/files", method = RequestMethod.GET)
    public List<AnonFile> getFiles() {
        return (List<AnonFile>) anonFileRepository.findAll();
    }

    @RequestMapping(path = "/counts", method = RequestMethod.GET)
    public HashMap getCounts() {
        HashMap counts = new HashMap();
        if (anonFileRepository.count() >= 1){
            counts.put("perm", anonFileRepository.findByIsPermanentTrue().size());
            counts.put("all", anonFileRepository.count());
        }
        return counts;
    }


}