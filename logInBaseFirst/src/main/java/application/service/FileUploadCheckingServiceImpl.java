package application.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import application.exception.InvalidFileUploadException;
@Service
public class FileUploadCheckingServiceImpl implements FileUploadCheckingService{
	private final List<String> ALLOWED_EXT = Arrays.asList("PNG","CSV","TIF","JPG","JPEG");
	
	public boolean checkFile(MultipartFile file) throws InvalidFileUploadException {
		//long size = file.getSize();
		System.err.println(file.getContentType() +   "   " + file.getName() +  "   " + file.getOriginalFilename());
		if ( file.getSize() > 50000000 ){
			throw new InvalidFileUploadException("File size exceeds 50MB");
		}
		String ext = file.getOriginalFilename().split("\\.")[1];
		if (!ALLOWED_EXT.contains(ext.toUpperCase())){
			throw new InvalidFileUploadException("Invalid file type");
		}
		
		
		return true;
	}

}
