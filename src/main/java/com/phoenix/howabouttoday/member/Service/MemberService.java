package com.phoenix.howabouttoday.member.Service;

//import com.phoenix.howabouttoday.global.ImageUtil;
import com.phoenix.howabouttoday.global.MailUtil;
import com.phoenix.howabouttoday.member.dto.MailDTO;
import com.phoenix.howabouttoday.member.dto.MemberDTO;
import com.phoenix.howabouttoday.member.entity.Member;
import com.phoenix.howabouttoday.member.entity.Role;
import com.phoenix.howabouttoday.member.repository.MemberRepository;
import com.phoenix.howabouttoday.payment.CouponFactory;
import com.phoenix.howabouttoday.payment.entity.Coupon;
import com.phoenix.howabouttoday.payment.entity.CouponRules;
import com.phoenix.howabouttoday.payment.enumType.CouponNumber;
import com.phoenix.howabouttoday.payment.enumType.CouponStatus;
import com.phoenix.howabouttoday.payment.enumType.DiscountType;
import com.phoenix.howabouttoday.payment.repository.CouponRepository;
import com.phoenix.howabouttoday.payment.repository.CouponRulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder encoder;
    private final CouponFactory couponFactory;

    @Transactional
    public Long join(MemberDTO DTO) {
        DTO.setPwd(encoder.encode(DTO.getPwd()));

        Member member = memberRepository.save(DTO.toEntity());
        couponFactory.create(CouponNumber.FIRST, member);

        return member.getMemberNum();
    }
//    ?????? join ?????????
//    @Transactional
//    public Long join(MemberDTO DTO) {
//        DTO.setPwd(encoder.encode(DTO.getPwd()));
//
//        return memberRepository.save(DTO.toEntity()).getMemberNum();
//    }


    /* ???????????? ???, ????????? ?????? */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        //????????? ????????? ????????? ?????? ????????? ??????
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /* ???????????? (dirty checking) */
    @Transactional
    public void modify(MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberDTO.getMemberNum()).orElseThrow(() ->
                new IllegalArgumentException("?????? ????????? ???????????? ????????????."));


        String encPassword = encoder.encode(memberDTO.getPwd());
        member.modify(encPassword,memberDTO.getNickname(),memberDTO.getMemberTel());

    }

    // ???????????? ??????
    public Member pwdFind(String email) throws Exception {

        // ???????????? ????????????
        Member member = memberRepository.findByEmail(email).orElse(null);

        // ????????? ??????
        if(member!=null) {

            // ?????? ???????????? ??????
            String tempPwd = UUID.randomUUID().toString().replace("-", ""); // - ??????
            tempPwd = tempPwd.substring(0, 10); // 10????????? ??????

            System.out.print("?????? ???????????? : " + tempPwd);
            member.findPwd(tempPwd);

            // ????????? ??????
            MailUtil mail = new MailUtil();
            mail.sendMail(member);

            // ???????????? ?????? ???????????? ??????
            member.findPwd(passwordEncoder.encode(member.getPwd()));

            memberRepository.save(member);
        }
        return member;

    }

    // ???????????? ???????????? ?????? ?????? ??????
    // ????????????
    public Member checkPwd(String email, String pwd) {
        return memberRepository.findByEmail(email) // email??? ????????? ????????????
                .filter(m -> this.passwordEncoder.matches(pwd, m.getPwd())) // ????????? pwd??? ???????????? pwd(m.getPwd)??? ?????????, ????????? ????????????
                .orElse(null); // ????????? null??? ????????????
    }

    // ?????? ??????
    @Transactional
    public void withdraw(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);
        memberRepository.delete(member);
    }



    public MemberDTO getSessionUser(Long memberNum){
        Member member = memberRepository.findById(memberNum).orElseThrow(() -> new IllegalArgumentException(String.format("%d??? ?????? ????????? ????????????.", memberNum)));

        return MemberDTO.builder()
                .memberNum(member.getMemberNum())
                .email(member.getEmail())
                .pwd(member.getPwd())
                .nickname(member.getNickname())
                .memberTel(member.getMemberTel())
                .role(member.getRole())
                .build();
    }

    public MemberDTO getAuthUser(String email){

        Member member = memberRepository.findByEmail(email).get();

        return MemberDTO.builder()
                .memberNum(member.getMemberNum())
                .email(member.getEmail())
                .pwd(member.getPwd())
                .nickname(member.getNickname())
                .memberTel(member.getMemberTel())
                .role(member.getRole())
                .build();
    }

    public MemberDTO getCustomer(Long memberNum) throws UsernameNotFoundException {

        Member member = memberRepository.findById(memberNum).get();

        MemberDTO customer = MemberDTO.builder()
                .memberNum(member.getMemberNum())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .memberTel(member.getMemberTel())
                .role(member.getRole())
                .build();

        return customer;
    }


}


