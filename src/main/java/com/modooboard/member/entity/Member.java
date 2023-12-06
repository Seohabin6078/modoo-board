package com.modooboard.member.entity;

import com.modooboard.audit.Auditable;
import com.modooboard.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "profile_image", length = 100)
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "member_status", nullable = false, length = 100)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", length = 100)
    private SocialType socialType;

    @Column(name = "social_id", length = 100)
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    @ElementCollection(fetch = FetchType.EAGER) // todo 이 부분 일대다 단방향 매핑과 영속성 전이와 고아객체 제거 활성화로 바꾸는거 고려해보기!
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "member_id"))
    private List<String> roles;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) // 회원을 삭제하면 관련된 게시글들도 삭제된다.
    private List<Post> postList = new ArrayList<>();

    @Builder
    public Member(Long memberId, String email, String password, String displayName, String profileImage, SocialType socialType, String socialId, List<String> roles, List<Post> postList) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.socialType = socialType;
        this.socialId = socialId;
        this.roles = roles;
        this.postList = postList;
    }

    public void createRoles(List<String> roles) {
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
