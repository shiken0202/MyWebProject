package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	
	@Query("select  s from Store s where s.user.id=:userId ")
	public Optional<Store> findStoreByUserId(@Param("userId") Long userId);
}
