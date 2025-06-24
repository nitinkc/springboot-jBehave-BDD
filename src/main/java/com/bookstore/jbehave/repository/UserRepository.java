package com.bookstore.jbehave.repository;


import com.bookstore.jbehave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
