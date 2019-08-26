package com.sdehunt.controller;

import com.sdehunt.dto.CvUrlDTO;
import com.sdehunt.dto.UploadCvUrlRequestDTO;
import com.sdehunt.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cv")
public class CvController {

    @Autowired
    private CvService cvService;

    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    public CvUrlDTO getUploadUrl(@RequestBody UploadCvUrlRequestDTO request) {
        return new CvUrlDTO().setUrl(cvService.uploadUrl(request.getUserId(), request.getFileName()));
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/download")
//    public CvUrlDTO getDownloadUrl(@PathVariable("userId") String userId) {
//        return new CvUrlDTO().setUrl(cvService.downloadUrl(userId));
//    }

}
