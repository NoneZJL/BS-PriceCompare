package org.zjubs.pricecomwebbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.zjubs.pricecomwebbackend.entity.Good;

import java.util.List;

@Mapper
public interface SnMapper {
    @Insert("insert into sn_items (price, description, img, shop_name, query_name, detail_url) value (#{price}, #{description}, #{img}, #{shopName}, #{queryName}, #{detailUrl})")
    public void insetItems(Good good);

    @Select("select * from sn_items where query_name = #{name}")
    public List<Good> queryGoodsBySearchingName(String name);

    @Select(("select * from sn_items where detail_url = #{url}"))
    public List<Good> queryFoodsByDetailUrl(String url);
}
