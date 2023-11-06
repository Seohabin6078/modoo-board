package com.modooboard.member.service;

import com.modooboard.member.entity.Member;

import java.util.List;

public interface MemberService {
    Member createMember(Member member);

    Member updateMember(Member member);

    Member findMember(Long memberId);

    List<Member> findAllMembersOrderByCreatedAtDesc();

    Member deleteMember(Long memberId);
}
