package com.app.bookstore.transformer;

import java.util.Optional;

import com.app.bookstore.domain.Promotion;
import com.app.bookstore.domain.dto.PromotionDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Promotion dto to Promotion entity, 
 * 2) convert Promotion entity to Promotion dto,
 */
public class PromotionTransformer extends AbstractTransformer implements Function<Promotion, PromotionDTO> {

	@Override
	public PromotionDTO apply(Promotion promotion) {
		return transform(promotion);
	}

	public static PromotionDTO transform(Promotion promotion) {
		Optional<PromotionDTO> optionalPromotionDTO = convertDbPromotionToPromotionDTO(promotion);
		if (optionalPromotionDTO.isPresent()) {
			return optionalPromotionDTO.get();
		}
		return null;
	}

	public static Promotion transform(final PromotionDTO promotionDTO) {
		Optional<Promotion> optionalPromotion = convertPromotionDTOToDbPromotion(promotionDTO);
		if (optionalPromotion.isPresent()) {
			return optionalPromotion.get();
		}
		return null;
	}

}
