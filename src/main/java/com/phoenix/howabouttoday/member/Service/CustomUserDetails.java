package com.phoenix.howabouttoday.member.Service;


import com.phoenix.howabouttoday.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
 * 스프링 시큐리티가 로그인 요청을 가로채 로그인을 진행하고 완료 되면 UserDetails 타입의 오브젝트를
 * 스프링 시큐리티의 고유한 세션저장소에 저장 해준다.
 * */

@AllArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    //계정 만료 여부
    //true : 만료 안됨
    //false : 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부
    //true : 잠기지 않음
    //false : 잠김
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    //true : 만료 안됨
    //false : 만료
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //사용자 활성화 여부
    //true : 만료 안됨
    //false : 만료
    @Override
    public boolean isEnabled() {
        return true;
    }

    //유저의 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> "ROLE_" + member.getRole());

        return collectors;
    }

}


