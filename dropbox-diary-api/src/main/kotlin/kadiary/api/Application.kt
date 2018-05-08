package kadiary.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableMongoAuditing // make @CreatedDate working
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}