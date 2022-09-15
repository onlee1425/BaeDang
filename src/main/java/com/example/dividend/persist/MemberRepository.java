package com.example.dividend.persist;

import com.example.dividend.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    //id를 기준으로 회원정보 찾기
    Optional<MemberEntity> findByUsername(String username);

    //회원가입시 중복id 존재 확인
    boolean existsByUsername(String username);
}
