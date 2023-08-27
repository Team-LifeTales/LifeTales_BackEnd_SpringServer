package com.LifeTales.global.Validation;

import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedUploadDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Component
public class FeedUploadValidator implements com.LifeTales.global.Validator.FeedUploadValidator {
    @Override
    public String feedUploadValidate(FeedUploadDTO feedUploadDTO) {
        String content = feedUploadDTO.getContent();
        List<byte[]> uploadIMGs = feedUploadDTO.getUploadImages();
// Check count: 7
        if (content == null) {
            return "내용을 입력해주세요.";
        }

        if (content.length() > 200 ) {
            return "내용은 200글자를 넘을 수 없습니다";
        }

        if (uploadIMGs == null){
            return "사진을 선택해주세요, 선택할 사진이 없을 시 AI사진추천을 눌러주세요";
        }
        return "Success"; // 모든 조건을 통과하면 성공
    }

    // 영문, 숫자, 특수문자 모두 포함하는지 체크하는 함수

}
