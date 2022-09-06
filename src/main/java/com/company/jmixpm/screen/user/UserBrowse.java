package com.company.jmixpm.screen.user;

import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends StandardLookup<User> {

    @Autowired
    private DataManager dataManager;
}