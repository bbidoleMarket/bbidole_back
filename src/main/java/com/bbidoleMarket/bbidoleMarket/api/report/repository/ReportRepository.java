package com.bbidoleMarket.bbidoleMarket.api.report.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.report.Report;
import java.util.Optional;

import com.bbidoleMarket.bbidoleMarket.api.entity.report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Override
    <S extends Report> S save(S report);

    @Override
    Optional<Report> findById(Long id);
}
