package application.service;

import org.springframework.web.multipart.MultipartFile;

import application.exception.InvalidFileUploadException;

public interface FileUploadCheckingService {

	boolean checkFile(MultipartFile file) throws InvalidFileUploadException;
	
}
