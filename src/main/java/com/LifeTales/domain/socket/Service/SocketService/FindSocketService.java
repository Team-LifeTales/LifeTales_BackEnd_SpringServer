package com.LifeTales.domain.socket.Service.SocketService;

import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import com.LifeTales.domain.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindSocketService {

    @Autowired
    private final FamilyRepository familyRepository;

    public List<FamilySearchDTO> find_family_service(String msg){
        log.info("find_family_service Start Msg : {}" ,msg);

        List<Family> result;

        //result = familyRepository.findFamilyByNickNameContaining(msg);
        result = familyRepository.findByNickNameContaining(msg);

        List<FamilySearchDTO> familySearchDTOList = new ArrayList<>();

        //List<String> nickNames = new ArrayList<>();

        for (Family family : result) {
            FamilySearchDTO dto = new FamilySearchDTO(family.getSeq(), family.getNickName());
            familySearchDTOList.add(dto);
        }

        return familySearchDTOList;
    }


}
