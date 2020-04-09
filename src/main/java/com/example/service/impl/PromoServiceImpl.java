package com.example.service.impl;

import com.example.dao.PromoDOMapper;
import com.example.dataobject.PromoDO;
import com.example.error.PromoStatus;
import com.example.service.PromoService;
import com.example.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        if (promoDO == null){
            return null;
        }
        PromoModel promoModel = convertModelFromDataObject(promoDO);

        if (DateTime.now().isBefore(promoModel.getStartDate())){
            promoModel.setPromoStatus(PromoStatus.NOT_START);
        } else if (DateTime.now().isAfter(promoModel.getEndDate())){
            promoModel.setPromoStatus(PromoStatus.HAVE_DONE);
        } else {
            promoModel.setPromoStatus(PromoStatus.ON_GOING);
        }
        return promoModel;
    }

    private PromoModel convertModelFromDataObject(PromoDO promoDO) {
        if (promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        promoModel.setPromoPrice(new BigDecimal(promoDO.getPromoPrice()));
        return promoModel;
    }
}
