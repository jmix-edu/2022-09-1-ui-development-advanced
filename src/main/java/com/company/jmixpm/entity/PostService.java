package com.company.jmixpm.entity;

import io.jmix.core.common.util.Preconditions;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PostService {

    public List<Post> fetchPosts() {
        RestTemplate rest = new RestTemplate();
        Post[] posts = rest.getForObject("https://jsonplaceholder.typicode.com/posts", Post[].class);
        return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    @Nullable
    public UserInfo fetchUserInfo(Long id) {
        Preconditions.checkNotNullArgument(id);

        RestTemplate rest = new RestTemplate();
        return rest.getForObject("https://jsonplaceholder.typicode.com/users/{id}", UserInfo.class, id);
    }
}