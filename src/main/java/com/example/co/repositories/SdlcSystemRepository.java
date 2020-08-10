package com.example.co.repositories;

import com.example.co.model.SdlcSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdlcSystemRepository extends JpaRepository<SdlcSystem, Long>{

}
