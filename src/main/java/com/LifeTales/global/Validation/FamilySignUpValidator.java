package com.LifeTales.global.Validation;

import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class FamilySignUpValidator implements com.LifeTales.global.Validator.FamilySignUpValidator {
    @Override
    public String familySignUpValidate(FamilySignUpDTO familySignUpDTO) {

        String nickName = familySignUpDTO.getNickName();
        String introduce = familySignUpDTO.getIntroduce();
        String familyQuestion = familySignUpDTO.getFamilySignInQuestion();
        String familyAnswer = familySignUpDTO.getFamilySignInAnswer();

// Check count: 7
        if (nickName == null || familyQuestion == null || familyAnswer==null) {
            return"가족 이름과 가족 질문, 가족 답변은 필수입니다.";
        }

        if (introduce.length() > 50 ) {
            return "소개글은 50글자를 넘을 수 없습니다";
        }

        // NickName: characters 2 <= x <= 10, Excluding special symbols
        //if (nickName.length() < 2 || nickName.length() > 10 || !isAlphaNumeric(nickName)) {
        if (nickName.length() < 2 || nickName.length() > 10 ) {

            return "NickName은 2자 이상 10자 이하이며, 특수문자는 허용되지 않습니다.{}"+nickName.length();
        }

        if (familyQuestion.length() > 50 ) {
            return "가족 가입을 위한 질문은 50글자 이내로 작성해주세요";
        }


        if (familyAnswer.length() > 50 ) {
            return "가족 가입을 위한 대답은 50글자 이내로 작성해주세요";
        }

        return "Success"; // 모든 조건을 통과하면 성공
    }

    // 영문, 숫자, 특수문자 모두 포함하는지 체크하는 함수


    // 알파벳과 숫자로만 이루어져 있는지 체크하는 함수
    private boolean isAlphaNumeric(String input) {
        for (char ch : input.toCharArray()) {
            if (!Character.isLetterOrDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    // 유효한 생년월일 형식인지 체크하는 함수

}
