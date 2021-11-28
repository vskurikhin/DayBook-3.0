package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    @ConsumeEvent("greetings")
    public String hello(String name) {
        return "Hello " + name;
    }
}
