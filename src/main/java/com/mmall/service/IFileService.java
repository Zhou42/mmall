package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yangzhou on 2017-07-23.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
