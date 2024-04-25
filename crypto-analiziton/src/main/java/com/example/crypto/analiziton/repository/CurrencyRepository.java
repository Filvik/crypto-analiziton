package com.example.crypto.analiziton.repository;

import com.example.crypto.analiziton.model.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    @Query("SELECT c FROM CurrencyEntity c WHERE c.currencyName = :currencyName AND c.createdAt >= :dateStart AND c.createdAt <= :dateStop")
    List<CurrencyEntity> findAllByCurrencyNameAndPeriodOfTime(
            @Param("currencyName") String currencyName,
            @Param("dateStart") Timestamp dateStart,
            @Param("dateStop") Timestamp dateStop);
}
