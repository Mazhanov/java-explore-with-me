package ru.practicum.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;

@Component
public class StatRestClient extends StatsClient {

    @Autowired
    public StatRestClient(@Value("${stats-server.url}") String url, RestTemplateBuilder builder) {
        super(url, builder);
    }
}
