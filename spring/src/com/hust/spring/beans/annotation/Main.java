package com.hust.spring.beans.annotation;

import com.hust.spring.beans.annotation.Controller.UserController;
import com.hust.spring.beans.annotation.Service.UserService;
import com.hust.spring.beans.annotation.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-annotation.xml");

//        TestObject to = (TestObject) ctx.getBean("testObject");
//        System.out.println(to);

        UserController userController = (UserController) ctx.getBean("userController");
        System.out.println(userController);
        userController.execute();

//        UserService userService = (UserService) ctx.getBean("userService");
//        System.out.println(userService);
//
//        UserRepository userRepository = (UserRepository) ctx.getBean("userRepository");
//        System.out.println(userRepository);
    }
}
