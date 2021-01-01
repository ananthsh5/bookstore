package com.app.bookstore.domain.dto;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class ChangePasswordDto {
    private String currentPassword;
    
    private String newPassword;
    
    private String confirmNewPassword;
}
