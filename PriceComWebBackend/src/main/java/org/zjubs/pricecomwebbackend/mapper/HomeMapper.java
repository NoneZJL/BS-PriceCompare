package org.zjubs.pricecomwebbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zjubs.pricecomwebbackend.query.UserInfo;

@Mapper
public interface HomeMapper {
    @Select("select * from user where username = #{username}")
    public UserInfo queryUserInfoByUsername(String username);

    @Update("update user set phone = #{phone}, address = #{address} where username = #{username}")
    public void updateUserInfo(String phone, String address, String username);
}
