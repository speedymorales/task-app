package com.taskapp.taskapp.repositories;

import java.util.List;

import com.taskapp.taskapp.entities.User;

import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByEmail(String email);
}