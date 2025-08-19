package com.startup.usergroup.repositories;

import com.startup.usergroup.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}