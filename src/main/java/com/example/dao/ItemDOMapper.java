package com.example.dao;

import com.example.dataobject.ItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    List<ItemDO> itemList();

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    void increaseSales(@Param("id") Integer id, @Param("quantity") Integer quantity);
}