package com.LifeTales.domain.test.service;

import com.LifeTales.domain.test.domain.Test;
import com.LifeTales.domain.test.repository.LifeTalesTestRepository;
import com.LifeTales.domain.test.repository.dto.TestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LifeTalesTestService {
    @Autowired
    private final LifeTalesTestRepository lifeTalesTestRepository;
    public Test dbtest_read_service(@RequestBody String name){
        log.info("dbtest_service Start");
        Test testData = new Test();
        testData = lifeTalesTestRepository.findByName(name);
        return testData;
    }

    public String dbtest_create_service(@RequestBody TestDTO test) {
        lifeTalesTestRepository.save(
                Test.builder()
                        .name(test.getName())
                        .age(test.getAge())
                        .NickName(test.getNickName())
                        .build()
        );
        return "Success";
    }
}
