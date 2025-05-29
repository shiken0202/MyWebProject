package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	
	@Query("select  s from Store s where s.user.id=:userId ")
	public Optional<Store> findStoreByUserId(@Param("userId") Long userId);
	
	@Modifying
	@Transactional
	@Query("update Store s set s.description =:description where s.id=:id")
	public  int updateStoreDescription(@Param("description")String description,@Param("id")Long id);
}
