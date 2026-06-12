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
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据绑定码查找用户
     */
    Optional<User> findByBindCode(String bindCode);

    /**
     * 判断OpenID是否存在
     */
    boolean existsByOpenid(String openid);

    /**
     * 判断手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 判断绑定码是否存在
     */
    boolean existsByBindCode(String bindCode);
}
