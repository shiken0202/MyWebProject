package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.UserListDto;
import com.example.demo.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 自訂方法：根據 username 查找用戶（用於檢查是否重複）
    public Optional<User> findByUserName(String username);

    @Query(value = "select id,user_name,email,email_confirmok,role,isbanned from users", nativeQuery = true)
    public List<UserListDto> findAllUsersInfo();

    public boolean existsByUserName(String userName);

    // 自訂方法：根據 email 查找用戶（用於檢查是否重複）
    Optional<User> findByEmail(String email);

    // email 驗證成功並修改 emailConfirmOK = true
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.emailConfirmOK = true WHERE u.userName = :username")
    public int emailConfirmOK(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isBanned = true WHERE u.id = :id")
    public int BlockUser(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isBanned = false WHERE u.id = :id")
    public int unBlockUser(@Param("id") Long id);

}
