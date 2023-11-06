package com.modooboard.member.repository;

import com.modooboard.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByDisplayName(String displayName);

    List<Member> findAllByOrderByCreatedAtDesc();

    Optional<Member> findBySocialTypeAndSocialId(Member.SocialType socialType, String socialId);
}
