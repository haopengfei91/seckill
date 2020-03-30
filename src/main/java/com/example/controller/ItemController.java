package com.example.controller;

import com.example.controller.viewobject.ItemVO;
import com.example.error.BusinessException;
import com.example.returntype.CommonReturnType;
import com.example.service.ItemService;
import com.example.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;

    @PostMapping(value = "/create", consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType ceateItem(@RequestParam(name="title") String title,
                                      @RequestParam(name="description") String description,
                                      @RequestParam(name="price") BigDecimal price,
                                      @RequestParam(name="stock") Integer stock,
                                      @RequestParam(name="imgUrl") String imgUrl) throws BusinessException {

        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        itemModel = itemService.creatItem(itemModel);
        ItemVO itemVO = convertVOFromMode(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertVOFromMode(ItemModel itemModel) {

        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        return itemVO;
    }
}
