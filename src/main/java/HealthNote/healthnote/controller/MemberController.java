package HealthNote.healthnote.controller;

import HealthNote.healthnote.domain.Member;
import HealthNote.healthnote.member_dto.*;
import HealthNote.healthnote.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/api/member/sign-up")
    public SignUpDto signUp(@RequestBody FormDto formDto) {
        return memberService.signUp(formDto);
    }

    // 로그인
    @PostMapping("/api/member/sign-in")
    public SignInDto signIn(@RequestBody FormDto formDto) {
        SignInDto result = memberService.signIn(formDto);
        // result가 null이 아니면 result를 반환하고, null이면 new MemberProcessResult(null, 400)을 반환
        return Objects.requireNonNullElseGet(result, () -> new SignInDto(null, 400));
    }

    //아이디 찾기
    @PostMapping("/api/member/find-userId")
    public FindIdDto findUserId(@RequestBody FormDto formDto) {
        Member result = memberService.findUserId(formDto);
        if (result != null) {
            return new FindIdDto(result.getUserId(), 200);
        } else {
            return new FindIdDto(null, 400);
        }
    }

    // 비밀번호 찾기
    @PostMapping("/api/member/find-userPass")
    public FindPwDto findUserPass(@RequestBody FormDto formDto) {
        return memberService.findUserPass(formDto);
    }

    // 비밀번호 재설정
    @PostMapping("/api/member/update-userPass")
    public UpdateUserPassDto updateUserPass(@RequestBody FormDto formDto) {
        return memberService.updateUserPass(formDto);
    }


    // 회원 소개문구 추가
    @PostMapping("/api/member/edit-profile")
    public EditProfileDto introductionDto(@RequestBody EditProfileFormDto editProfileFormDto) {
        return memberService.editProfile(editProfileFormDto);
    }

    // 마이페이지 정보


    //회원탈퇴
    @DeleteMapping("/api/member/withdrawal")
    public WithdrawalDto WithdrawalMember(@RequestParam("memberId") Long memberId) {
        boolean success = memberService.WithdrawalMemberService(memberId);
        return new WithdrawalDto(success, 200, "탈퇴 완료");
    }

}
