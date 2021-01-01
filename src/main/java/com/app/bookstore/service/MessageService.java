package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UserDTO;

/**
 * @author Ananth Shanmugam
 */
public interface MessageService {
    
    List<Message> getMessages();

    MessageDTO getMessageById(Long id);

    void sendMessageToUser(UserDTO toUser, String content);
    
    void setMessageRead(Long id);

    Message saveMessage(MessageDTO message);

    void delete(MessageDTO message);

}
