package com.saju.mapper;

import com.saju.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByProviderAndUserId(@Param("provider") String provider, @Param("userId") String userId);
    void insert(User user);
    void update(User user);
}
