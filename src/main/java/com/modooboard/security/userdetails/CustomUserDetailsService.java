package com.modooboard.security.userdetails;

import com.modooboard.exception.BusinessLogicException;
import com.modooboard.exception.ExceptionCode;
import com.modooboard.member.entity.Member;
import com.modooboard.member.repository.MemberRepository;
import com.modooboard.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new CustomUserDetails(findMember);
    }

    private final class CustomUserDetails extends Member implements UserDetails {
        CustomUserDetails(Member member) {
            updateMember(
                    member.getMemberId(),
                    member.getEmail(),
                    member.getPassword(),
                    member.getDisplayName()
            );
            createRoles(member.getRoles());
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
