package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Promotion;
import com.app.bookstore.domain.dto.PromotionDTO;
import com.app.bookstore.repository.PromotionRepository;
import com.app.bookstore.service.PromotionService;
import com.app.bookstore.transformer.PromotionTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class PromoServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public List<PromotionDTO> getAll() {   /* Get all promos from db  */
        final List<Promotion> results = (List<Promotion>)promotionRepository.findAll();
        List<PromotionDTO> resultList = new ArrayList<PromotionDTO>();
        if ( results != null ) {
			results.stream().forEach(promotionObj -> resultList.add(PromotionTransformer.transform(promotionObj)));
        }
        return resultList;
    }

    @Override
    public PromotionDTO savePromo(PromotionDTO promotionDTO) { /* Save the promo to db  */
    	Promotion savedPromotion = promotionRepository.save(PromotionTransformer.transform(promotionDTO));
    	return PromotionTransformer.transform(savedPromotion);
    }

    @Override
    public PromotionDTO getPromoById(Long id) { /* Get the promo by id from db  */
        return PromotionTransformer.transform(promotionRepository.findById(id).get());
    }

    @Override
    public void deletePromo(PromotionDTO promotion) { /* Delete the promo by id in db  */
        promotionRepository.delete(PromotionTransformer.transform(promotion));
    }


}
