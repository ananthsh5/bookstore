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
import com.app.bookstore.domain.dto.PromotionDTO;

/**
 * @author Ananth Shanmugam
 * Class to validate front end image upload for promotion
 */
@Component
public class PromotionImageFileValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return  PromotionDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PromotionDTO promotionDTO = (PromotionDTO) target;
        MultipartFile uploadPromo = promotionDTO.getImageUpload();
        String homeUrl = new ApplicationHome(BookStoreAppLauncher.class).getDir() + "/static/img/promos";
        Path rootLocation = Paths.get(homeUrl);

        if (!Files.exists(rootLocation)) {
            try {
                Files.createDirectory(rootLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (uploadPromo != null && !uploadPromo.isEmpty()) {
            try {
                String promoName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(uploadPromo.getOriginalFilename());
                Files.copy(uploadPromo.getInputStream(), rootLocation.resolve(promoName));
                promotionDTO.setImage("/img/promos/" + promoName);
            } catch (Exception ex) {
            	errors.rejectValue("uploadPromo", "", "Problem on saving promo picture.");
            }
        }

	}
}
