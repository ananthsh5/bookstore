package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.repository.PublisherRepository;
import com.app.bookstore.service.PublisherService;
import com.app.bookstore.transformer.CustomerTransformer;
import com.app.bookstore.transformer.PublisherTransformer;
import com.app.bookstore.transformer.UserTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    PublisherRepository publisherRepository;

    @Override
    public PublisherDto getPublisherById(Long id) { /* Get publisher by id from db  */
        return PublisherTransformer.transform(publisherRepository.findById(id).get());
    }

    @Override
    public PublisherDto save(PublisherDto publisherDTO) {  /* Save the new publisher to db */

        final Publisher publisher = publisherRepository.save(PublisherTransformer.transform(publisherDTO));
        return PublisherTransformer.transform(publisher);
    }

    @Override
    public PublisherDto getPublisherByUser(UserDTO user) { /* Get the publisher for user from db */
    	Publisher publisher = publisherRepository.findPublisherByUser(UserTransformer.transform(user));
    	return PublisherTransformer.transform(publisher);
    }

    @Override
    public List<CustomerDTO> getSubscribers(Long publisherId) { /* Get the subscribers for publisher from db */ 
        
    	
    	final List<Customer> results = publisherRepository.findById(publisherId).get().getCustomers();
    	List<CustomerDTO> resultList = new ArrayList<CustomerDTO>();
        if ( results != null ) {
			results.stream().forEach(customerObj -> resultList.add(CustomerTransformer.transform(customerObj)));
        }
        return resultList;

    }

    public PublisherDto updatePublisher(PublisherDto publisherDTO) { /* Update the publisher profile in db */ 
    	Publisher selectedPublisher = publisherRepository.findById(publisherDTO.getId()).get();
        selectedPublisher.setName(publisherDTO.getName());
        selectedPublisher.setDescription(publisherDTO.getDescription());
        selectedPublisher.setPicture(publisherDTO.getPicture());
        Publisher publisher = publisherRepository.save(selectedPublisher);
        return PublisherTransformer.transform(publisher);
    }

    @Override
    public List<PublisherDto> getAllPublishers() {   /* Get all publishers from db  */
        
        final Iterable<Publisher> results = publisherRepository.findAll();
        List<PublisherDto> resultList = new ArrayList<PublisherDto>();
        if ( results != null ) {
			results.forEach(customerObj -> resultList.add(PublisherTransformer.transform(customerObj)));
        }
        return resultList;
    }
    
}
