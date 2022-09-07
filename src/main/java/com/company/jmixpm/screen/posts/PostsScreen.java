package com.company.jmixpm.screen.posts;

import com.company.jmixpm.entity.Post;
import com.company.jmixpm.entity.PostService;
import com.company.jmixpm.screen.userinfo.UserInfoScreen;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Table;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("posts")
@UiController("PostsScreen")
@UiDescriptor("posts-screen.xml")
public class PostsScreen extends Screen {
    @Autowired
    private PostService postService;

    @Autowired
    private Table<Post> postsTable;
    @Autowired
    private ScreenBuilders screenBuilders;

    @Install(to = "postsDl", target = Target.DATA_LOADER)
    private List<Post> postsDlLoadDelegate(LoadContext<Post> loadContext) {
        return postService.fetchPosts();
    }

    @Install(to = "userInfoScreenFacet", subject = "screenConfigurer")
    private void userInfoScreenFacetScreenConfigurer(UserInfoScreen userInfoScreen) {
        Post selected = postsTable.getSingleSelected();
        if (selected == null || selected.getUserId() == null) {
            throw new IllegalStateException("No Post selected");
        }

        userInfoScreen.setUserId(selected.getUserId());
    }

    /*@Subscribe("postsTable.viewUserInfo")
    public void onPostsTableViewUserInfo(Action.ActionPerformedEvent event) {
        Post selected = postsTable.getSingleSelected();
        if (selected == null || selected.getUserId() == null) {
            return;
        }

        UserInfoScreen userInfoScreen = screenBuilders.screen(this)
                .withScreenClass(UserInfoScreen.class)
                .build();

        userInfoScreen.setUserId(selected.getUserId());

        userInfoScreen.show();
    }*/
}