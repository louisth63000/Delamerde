package com.example.restservice.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.example.restservice.Model.Message;

import com.example.restservice.Model.User;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message>{
    Page<Message> findByFromOrTo(User from, User to,Pageable pageable);
}
