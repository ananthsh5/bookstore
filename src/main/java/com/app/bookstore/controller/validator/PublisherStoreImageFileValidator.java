package com.app.bookstore.controller.validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.app.bookstore.BookStoreAppLauncher;
import com.app.bookstore.domain.dto.PublisherDto;

/**
 * @author Ananth Shanmugam
 * Class to validate physical store image upload for publisher 
 */
@Component
public class PublisherStoreImageFileValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return  PublisherDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PublisherDto publisherDto = (PublisherDto) target;
        MultipartFile uploadPicture = publisherDto.getUpload();
        String homeUrl = new ApplicationHome(BookStoreAppLauncher.class).getDir() + "\\static\\img\\publisher";
        Path rootLocation = Paths.get(homeUrl);

        if (!Files.exists(rootLocation)) {
            try {
                Files.createDirectory(rootLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String pictureName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(uploadPicture.getOriginalFilename());
        if (uploadPicture != null && !uploadPicture.isEmpty()) {
            try {
                Files.copy(uploadPicture.getInputStream(), rootLocation.resolve(pictureName));
                publisherDto.setPicture("/img/publisher/" + pictureName);
            } catch (Exception ex) {
                errors.rejectValue("uploadPicture", "", "Problem on saving publisher picture.");
            }
        }

	}
}
