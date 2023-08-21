package com.LifeTales.global.Validation;

import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class UserSignUpValidator implements com.LifeTales.global.Validator.UserSignUpValidator {
    @Override
    public String userSignUpValidate(UserSignUpDTO userSignUpDTO) {

        /**
         * Do :
         * check Count : 7
         * id : characters 6 <=  x < =30
         * PWD : characters 8 <=  x < =30 , 영 , 숫 , 특 (모두 포함되어있는지 확인)
         * name : characters 2 <=  x < =10 , only Korean
         * NickName : characters 2 <=  x < =10 , Excluding special symbols
         * birthDay : exc xxxx-xx-xx 허용가능한 생년월일인지 확인
         * phoneNumber : exc xxx -xxxx -xxxx : frist only 010
         */

        /**
         * work
         */

        String id = userSignUpDTO.getId();
        String PWD = userSignUpDTO.getPwd();
        String name = userSignUpDTO.getName();
        String nickName = userSignUpDTO.getNickName();
        LocalDate birthDay = userSignUpDTO.getBirthDay();
        String phoneNumber = userSignUpDTO.getPhoneNumber();

// Check count: 7
        if (id == null || PWD == null || name == null || nickName == null || birthDay == null || phoneNumber == null) {
            return"모든 필드는 필수입니다.";
        }

        if (id.length() < 6 || id.length() > 30) {
            return "id는 6자 이상 30자 이하이어야 합니다.";
        }

        // PWD: characters 8 <= x <= 30, 영, 숫, 특 (모두 포함되어 있는지 확인)
        if (PWD.length() < 8 || PWD.length() > 30 || !isPasswordValid(PWD)) {
            return "PWD는 8자 이상 30자 이하이며, 영문, 숫자, 특수문자를 모두 포함해야 합니다.";
        }

        // name: characters 2 <= x <= 10, only Korean
        if (name.length() < 2 || name.length() > 10 || !isKorean(name)) {
            return "name은 2자 이상 10자 이하이며, 한글만 허용됩니다.";
        }

        // NickName: characters 2 <= x <= 10, Excluding special symbols
        if (nickName.length() < 2 || nickName.length() > 10 || !isAlphaNumeric(nickName)) {
            return "NickName은 2자 이상 10자 이하이며, 특수문자는 허용되지 않습니다.";
        }

        // birthDay: exc xxxx-xx-xx 허용 가능한 생년월일인지 확인
        if (!isValidBirthDay(birthDay)) {
            return "유효한 생년월일 형식이 아니거나 허용 범위를 벗어났습니다.";
        }

        // phoneNumber: exc xxx-xxxx-xxxx : first only 010
        if (!isValidPhoneNumber(phoneNumber)) {
            return "유효한 전화번호 형식이 아닙니다.";
        }

        return "Success"; // 모든 조건을 통과하면 성공
    }

    // 영문, 숫자, 특수문자 모두 포함하는지 체크하는 함수
    private boolean isPasswordValid(String password) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        String specialChars = "!@#$%^&*()_+{}[]|\\:;<>,.?/~`-=";

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (specialChars.contains(String.valueOf(ch))) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    // 한글 여부를 체크하는 함수
    private boolean isKorean(String input) {
        for (char ch : input.toCharArray()) {
            if (!(Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                    Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ||
                    Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_JAMO)) {
                return false;
            }
        }
        return true;
    }

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
    private boolean isValidBirthDay(LocalDate birthDay) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minValidDate = LocalDate.of(1900, 1, 1); // 1900년 1월 1일부터 유효

        if (birthDay.isBefore(minValidDate) || birthDay.isAfter(currentDate)) {
            return false;
        }

        return true;
    }

    // 유효한 전화번호 형식인지 체크하는 함수
    private boolean isValidPhoneNumber(String phoneNumber) {
        // 전화번호의 길이는 11이어야 함 ("010" 다음 8자리 숫자)
        if (phoneNumber.length() != 11) {
            return false;
        }

        // 전화번호가 "010"으로 시작하는지 확인
        if (!phoneNumber.startsWith("010")) {
            return false;
        }

        // 전화번호의 나머지 8글자는 숫자로만 이루어져 있어야 함
        String digits = phoneNumber.substring(3); // "010" 다음의 부분 추출
        for (char ch : digits.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }
}
