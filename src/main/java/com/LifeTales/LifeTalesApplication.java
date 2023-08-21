package com.LifeTales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
		//s3를 위한 aws sdk 사용시 -> 아마존 환경이 아닌 로컬에서 돌릴 경우 실행시간이 약간 걸리는 오류 발생 -> endpoint에 연결을 못하는
		//에러인데 해당 옵션으로 안뜨게 할 수 있음 (에러 떠도 정상적으로 실행은 됨 .. 단지 어플리케이션 돌아가는 시간이 좀 오래걸림)
})
public class LifeTalesApplication {
	public static void main(String[] args) {
		SpringApplication.run(LifeTalesApplication.class, args);
	}
}
