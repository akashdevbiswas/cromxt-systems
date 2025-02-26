package com.cromxt.routeservice;


import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class DemoTest {

    private List<DemoUser> listOfUser = new ArrayList<>();

    @BeforeEach
    void generateUser(){
        for(int i = 0 ; i < 100;i++){
            listOfUser.add(new DemoUser("User"+i,i));
        }
    }

    static class UserComparator implements Comparator<DemoUser>{

        @Override
        public int compare(DemoUser o1, DemoUser o2) {
            return o2.getAge().compareTo(o1.getAge());
        }
    }

    @Test
    public void test(){

        PriorityQueue<DemoUser> userQueue = new PriorityQueue<>(new UserComparator());

//        userQueue.addAll(listOfUser);

        DemoUser demoUser1 = new DemoUser("User"+1,1);
        DemoUser demoUser2 = new DemoUser("User"+2,2);
        DemoUser demoUser3 = new DemoUser("User"+3,3);
        DemoUser demoUser4 = new DemoUser("User"+4,4);
        DemoUser demoUser5 = new DemoUser("User"+5,5);
        DemoUser demoUser6 = new DemoUser("User"+6,6);

        userQueue.add(demoUser6);
        userQueue.add(demoUser1);
        userQueue.add(demoUser3);
        userQueue.add(demoUser4);
        userQueue.add(demoUser2);
        userQueue.add(demoUser5);

       while (!userQueue.isEmpty()){
           DemoUser user = userQueue.poll();
           System.out.println(user);
       }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class DemoUser{
        private String name;
        private Integer age;
    }
}
