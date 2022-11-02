package io.shangyh;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.deergate.BullShit;
import io.github.deergate.BullShitData;
import io.github.deergate.JsonDeserialize;
import io.shangyh.dbcreator.User;
import io.shangyh.dbcreator.UserRepo;
import io.github.deergate.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// @SpringBootApplication
public class DatagenApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatagenApplication.class, args);
    }

    @Bean
    BullShit bullShit(ObjectMapper mapper) throws IOException {
        return new BullShit(mapper::readValue);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepo repo, BullShit bullShit) {
        return args -> {
            System.out.println("==================>start create users:");
            List<User> users = new ArrayList<>();
            Faker faker = new Faker();
            Faker chineseFaker = Faker.instance(Locale.SIMPLIFIED_CHINESE);
            for (int i = 1; i <= 10000; i++) {
                User user = new User();
                user.setAddress(chineseFaker.address().streetAddress());
                user.setCity(chineseFaker.address().city());
                user.setEmail(faker.internet().emailAddress());
                if (i % 4 == 0) {
                    user.setAddress(faker.address().streetAddress());
                    user.setCity(faker.address().city());
                    user.setName(faker.name().fullName());
                    user.setArticle(faker.howIMetYourMother().quote());
                } else {
                    user.setAddress(chineseFaker.address().streetAddress());
                    user.setCity(chineseFaker.address().city());
                    user.setName(chineseFaker.name().fullName());
                    //狗屁不通文章生成
                    user.setArticle(bullShit.generator("数据生成",500));
                }
                users.add(user);
            }
            repo.saveAll(users);
            System.out.println("==================>create users completed");
        };
    }
}
