package org.zjubs.pricecomwebbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zjubs.pricecomwebbackend.entity.User;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    public User queryUserByUsername(String username);

    @Select("select * from user where email = #{email}")
    public User queryUserByEmail(String email);

    @Select("select * from user where username = #{username} and password = #{password}")
    public User queryUserByUsernameAndPassword(String username, String password);

    @Select("select * from user where email = #{email} and password = #{password}")
    public User queryUserByEmailAndPassword(String email, String password);

    @Insert("insert into user (username, password, email) VALUES (#{username}, #{password}, #{email})")
    public void userRegister(String username, String password, String email);

    @Update("update user set password = #{password} where email = #{email}")
    public void modifyPasswordByEmail(String password, String email);

    @Select("select * from user where id = #{id};")
    public User getUserById(Integer id);
}