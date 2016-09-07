package me.icymint.sage.user.data.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.Instant;
import java.util.Date;

/**
 * Created by daniel on 16/9/3.
 */
@Mapper
public interface ClockMapper {

    @Select("SELECT CURRENT_TIMESTAMP")
    Instant now();
}
