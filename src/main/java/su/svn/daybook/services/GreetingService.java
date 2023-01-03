package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import su.svn.daybook.services.mappers.CodifierMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GreetingService {

    @Inject
    CodifierMapper codifierMapper;

    @ConsumeEvent("greetings")
    public String hello(String name) {
        return "Hello " + name;
    }
}
