package com.example.crypto.analiziton.repository;

import com.example.crypto.analiziton.model.DataFromBinanceAboutVolume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolumeRepository extends JpaRepository<DataFromBinanceAboutVolume, Long> {
}
