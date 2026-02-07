package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}
