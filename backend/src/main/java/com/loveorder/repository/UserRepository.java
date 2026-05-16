package com.loveorder.repository;

import com.loveorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据微信OpenID查找用户
     */
    Optional<User> findByOpenid(String openid);

    /**
     * 判断OpenID是否存在
     */
    boolean existsByOpenid(String openid);
}
