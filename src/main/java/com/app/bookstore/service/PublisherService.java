package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;

/**
 * @author Ananth Shanmugam
 */
public interface PublisherService {
    PublisherDto getPublisherById(Long id);
    PublisherDto save(PublisherDto publisher);
    PublisherDto getPublisherByUser(UserDTO user);
    List<CustomerDTO> getSubscribers(Long publisherId);
    PublisherDto updatePublisher(PublisherDto publisher);
    List<PublisherDto> getAllPublishers();
}
