package com.app.bookstore.service;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UpdateProfileDto;
import com.app.bookstore.domain.dto.UserDTO;

/**
 * @author Ananth Shanmugam
 */
public interface UserService {

    Boolean validatePassword(String password, UserDTO user);

    UserDTO addUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    UserDTO changePassword(String newPassword, UserDTO user);

    UserDTO findByEmail(String email);
    
    UpdateProfileDto getLoggedInUserProfile();

    Optional<List<MessageDTO>> getLast3UnreadNotifyMessageByUserEmail(String email);
}
