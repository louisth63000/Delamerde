package com.example.restservice.Repository;

import com.example.restservice.Model.Lot;
import com.example.restservice.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {


}