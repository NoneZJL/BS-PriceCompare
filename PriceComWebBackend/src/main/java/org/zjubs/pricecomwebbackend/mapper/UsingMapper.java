package org.zjubs.pricecomwebbackend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.zjubs.pricecomwebbackend.entity.History;
import org.zjubs.pricecomwebbackend.query.Remainder;

import java.util.List;

@Mapper
public interface UsingMapper {
    @Select("select * from history where user_id = #{id} order by time desc")
    List<History> getAllHistoryByUserId(Integer id);

    @Select("select * from history where user_id = #{id} and content = #{content}")
    History queryHistoryByUserIdAndContent(Integer id, String content);

    @Delete("delete from history where user_id = #{id} and content = #{content}")
    void deleteHistoryByUserIdAndContent(Integer id, String content);

    @Insert("insert into history (user_id, content) VALUE (#{id}, #{content})")
    void insertQueryHistory(Integer id, String content);

    @Delete("delete from history where user_id = #{id}")
    void deleteAllHistoryByUserId(Integer id);

    @Insert("insert into reminder (user_id, description, price, img, detail_url, `from`) VALUE (#{userId}, #{description}, #{price}, #{img}, #{detailUrl}, #{from})")
    void insertRemainder(Integer userId, String description, Double price, String img, String detailUrl, String from);

    @Select("select * from reminder where user_id = #{id}")
    List<Remainder> getRemaindersByUserId(Integer id);

    @Select("select * from reminder where id = #{id}")
    Remainder getReminderById(Integer id);

    @Delete("delete from reminder where id = #{id}")
    void deleteRemainderById(Integer id);

    @Select("select * from reminder")
    List<Remainder> getAllReminder();
}
