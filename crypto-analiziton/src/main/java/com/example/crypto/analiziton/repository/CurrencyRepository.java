package com.example.crypto.analiziton.repository;

import com.example.crypto.analiziton.model.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
}
