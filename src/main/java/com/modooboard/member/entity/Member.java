package com.modooboard.member.entity;

import com.modooboard.audit.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor // Builder 패턴을 사용하기 위해서는 파라미터가 있는 생성자가 필요하다
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 100)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "memberId"))
    private List<String> roles = new ArrayList<>();

    public void createRoles(List<String> roles) {
        this.roles = roles;
    }

    public void changeRoles(List<String> roles) {
        this.roles = roles;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void changeMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void updateMember(Long memberId, String email, String password, String displayName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }

    @Getter
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        private final String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public enum SocialType {
        GOOGLE, NAVER, KAKAO
    }
}
