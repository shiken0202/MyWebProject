package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User,Long>{
	 // 自訂方法：根據 username 查找用戶（用於檢查是否重複）
    Optional<User> findByUsername(String username);

    // 自訂方法：根據 email 查找用戶（用於檢查是否重複）
    Optional<User> findByEmail(String email);
		// email 驗證成功並修改 completed = true
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.emailConfirmOK = true WHERE u.username = :username")
	int emailConfirmOK(String username);
}
