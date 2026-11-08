package com.startup.usergroup.controllers;

import com.startup.usergroup.models.PoolGroup;
import com.startup.usergroup.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping
    public PoolGroup createGroup(@RequestBody PoolGroup poolGroup) {
        return groupRepository.save(poolGroup);
    }

    @GetMapping
    public List<PoolGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoolGroup> getGroupById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}