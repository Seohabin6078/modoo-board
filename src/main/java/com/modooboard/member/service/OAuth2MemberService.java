package com.modooboard.member.service;

import com.modooboard.exception.BusinessLogicException;
import com.modooboard.exception.ExceptionCode;
import com.modooboard.member.entity.Member;
import com.modooboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
//@Service
public class OAuth2MemberService implements MemberService {
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        if (!isExistsEmail(member.getEmail())) {
            return memberRepository.save(member);
        }

        return member;
    }

    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());

        return memberRepository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if (member.getMemberStatus() == Member.MemberStatus.MEMBER_SLEEP) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_STATUS_SLEEP);
        } else if (member.getMemberStatus() == Member.MemberStatus.MEMBER_QUIT) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_STATUS_QUIT);
        }
        return member;
    }

    // 최근 가입한 순서대로 모든 회원을 반환
    public List<Member> findAllMembersOrderByCreatedAtDesc() {
        return memberRepository.findAllByOrderByCreatedAtDesc();
    }

    // soft delete 적용
    public Member deleteMember(Long memberId) {
        Member member = findMember(memberId);
        member.changeMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        return memberRepository.save(member);
    }

    private boolean isExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.isPresent();
    }
}
