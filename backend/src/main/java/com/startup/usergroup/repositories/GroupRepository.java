package com.startup.usergroup.repositories;

import com.startup.usergroup.models.PoolGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<PoolGroup, Long> {}