package com.web.curation.service.fileupload;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServiceImpl implements FileUploadService{

	public void uploadFile(MultipartFile file) {
//		file.transferTo(new File(""));
	}
}
