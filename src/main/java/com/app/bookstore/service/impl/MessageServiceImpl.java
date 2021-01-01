package com.app.bookstore.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.repository.MessageRepository;
import com.app.bookstore.service.MessageService;
import com.app.bookstore.transformer.MessageTransformer;
import com.app.bookstore.transformer.UserTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message saveMessage(MessageDTO message) {  /* Save the message to db */ 
        return messageRepository.save(MessageTransformer.transform(message));
    }

    @Override
    public List<Message> getMessages() {  /* get all messages from db */
        return (List<Message>) messageRepository.findAll();
    }

    @Override
    public MessageDTO getMessageById(Long id) {  /* get message by id from db */
        return MessageTransformer.transform(messageRepository.findById(id).get());
    }

    @Override
    public void delete(MessageDTO messageDTO) {  /* Delete message in db */
        messageRepository.delete(MessageTransformer.transform(messageDTO));
    }

    @Override
    public void setMessageRead(Long id) {  /* Delete message in db */
        Message msg = messageRepository.findById(id).get();
        if(msg != null){
            msg.setRead(true);
            messageRepository.save(msg);
        }
    }

    @Override  
    public void sendMessageToUser(UserDTO toUser, String content) {  /* Send message to customer */
        Message msg = new Message();
        msg.setContent(content);
        msg.setReceivedDate(LocalDateTime.now());
        msg.setUser(UserTransformer.transform(toUser));
        msg.setRead(false);
        messageRepository.save(msg);
    }

}
