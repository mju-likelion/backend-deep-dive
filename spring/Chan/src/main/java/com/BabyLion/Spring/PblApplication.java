package com.BabyLion.Spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class PblApplication {
    public static void main(String[] args){
        SpringApplication.run(PblApplication.class, args);

        // 스프링 컨테이너 생성
        // ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 스프링이 MemberService타입의 Bean을 반환해줌
        // MemberService memberService = applicationContext.getBean(MemberService.class);

        // 반환한 Bean 확인
        //System.out.println("memberService: " + memberService);

        // 보너스 과제: 등록된 bean 출력
        //String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        //for(String beanDefinitionName : beanDefinitionNames){
        //System.out.println("Bean: " + beanDefinitionName);
        }
}
