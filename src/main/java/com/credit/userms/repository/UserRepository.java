package com.credit.userms.repository;

import com.credit.userms.entity.User;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(nativeQuery = true,value = "Select * from user where username=:username")
    public Optional<User> findByUsername(String username);


    @Query(nativeQuery = true, value = "delete from user where username=:username")
    public void deleteByUsername(String username);

    @Query(nativeQuery = true, value = "select * from user where email=:userEmail")
    User findByEmail(String userEmail);
}
