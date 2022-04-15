package com.sso.sso_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.sso_java.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;


/**
 * @author A
 * @date 2022/4/12 - 04 - 12 - 11:13
 * com.sso.sso_java.mapper
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
