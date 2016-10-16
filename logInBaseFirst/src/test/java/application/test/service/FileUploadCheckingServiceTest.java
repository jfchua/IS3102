package application.test.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import application.exception.InvalidFileUploadException;
import application.service.user.FileUploadCheckingService;
import application.test.AbstractTest;

@Transactional
public class FileUploadCheckingServiceTest extends AbstractTest {
	@Autowired
	private FileUploadCheckingService fileService;
	
	MultipartFile f;
	MultipartFile f2;

	@Before
	public void setUp(){
		Path path = Paths.get("images/algattasLogo.png");
		String name = "file.png";
		String originalFileName = "file.png";
		String contentType = "text/plain";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		f = new MockMultipartFile(name,
				originalFileName, contentType, content);
		
		
		Path path2 = Paths.get("");
		String name2 = "nonexistent.txt";
		String originalFileName2 = "nonexistent.txt";
		String contentType2 = "text/plain";
		byte[] content2 = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		f2 = new MockMultipartFile(name2,
				originalFileName2, contentType2, content2);
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testCheckFile() throws InvalidFileUploadException{
		boolean result = fileService.checkFile(f);
		Assert.assertTrue(result);
	}
	
	@Test(expected=InvalidFileUploadException.class)
	public void testCheckFileInvalid() throws InvalidFileUploadException{
		boolean result = fileService.checkFile(f2);
		Assert.assertFalse(result);
	}

}
