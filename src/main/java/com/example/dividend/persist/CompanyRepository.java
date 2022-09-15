package com.example.dividend.persist;

import com.example.dividend.persist.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    //배당금 정보 저장(ticker 기 존재 확인)
    boolean existsByTicker(String ticker);

    //회사명 기준 회사정보 조회
    Optional<CompanyEntity> findByName(String name);

    //회사명 keyword 의 대소문자 구분없이 조회
    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);

    //ticker를 기준으로 회사명 조회
    Optional<CompanyEntity> findByTicker(String ticker);
}
