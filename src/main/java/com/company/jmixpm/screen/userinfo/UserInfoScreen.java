package com.company.jmixpm.screen.userinfo;

import com.company.jmixpm.entity.PostService;
import com.company.jmixpm.entity.UserInfo;
import io.jmix.core.LoadContext;
import io.jmix.core.common.util.Preconditions;
import io.jmix.ui.action.Action;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("UserInfoScreen")
@UiDescriptor("user-info-screen.xml")
@EditedEntityContainer("userInfoDc")
@DialogMode(forceDialog = true, width = "AUTO", height = "AUTO")
public class UserInfoScreen extends Screen {

    @Autowired
    private PostService postService;

    private Long userId;

    public void setUserId(Long userId) {
        Preconditions.checkNotNullArgument(userId);

        this.userId = userId;
    }

    @Install(to = "userInfoDl", target = Target.DATA_LOADER)
    private UserInfo userInfoDlLoadDelegate(LoadContext<UserInfo> loadContext) {
        UserInfo userInfo = postService.fetchUserInfo(userId);
        if (userInfo == null) {
            throw new IllegalStateException("User wasn't lodded fro Id: " + userId);
        }

        return userInfo;
    }

    @Subscribe("windowClose")
    public void onWindowClose(Action.ActionPerformedEvent event) {
        closeWithDefaultAction();
    }
}