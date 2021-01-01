package com.app.bookstore.service;


import java.util.List;

import com.app.bookstore.domain.dto.PromotionDTO;

/**
 * @author Ananth Shanmugam
 */
public interface PromotionService {

    List<PromotionDTO> getAll();

    public PromotionDTO savePromo(PromotionDTO promotion);

    public PromotionDTO getPromoById(Long id);

    public void deletePromo(PromotionDTO promotion);

}
