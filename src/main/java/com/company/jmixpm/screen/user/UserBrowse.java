package com.company.jmixpm.screen.user;

import com.company.jmixpm.entity.User;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputDialog;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.component.data.TableItems;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.executor.BackgroundWorker;
import io.jmix.ui.executor.TaskLifeCycle;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@UiController("User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends StandardLookup<User> {

    @Autowired
    private Dialogs dialogs;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private GroupTable<User> usersTable;

    @Autowired
    private CollectionContainer<User> usersDc;
    @Autowired
    private Notifications notifications;

    @Subscribe("emailDialog")
    public void onEmailDialogInputDialogClose(InputDialog.InputDialogCloseEvent closeEvent) {
        if (closeEvent.closedWith(DialogOutcome.OK)) {
            String title = closeEvent.getValue("title");
            String body = closeEvent.getValue("body");

            Set<User> selected = usersTable.getSelected();
            Collection<User> users = selected.isEmpty()
                    ? usersDc.getItems()
                    : selected;

            doSendEmail(title, body, users);
        }
    }

    /*@Subscribe("usersTable.sendEmail")
    public void onUsersTableSendEmail(Action.ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Send Email")
                .withParameters(
                        InputParameter.stringParameter("title")
                                .withCaption("Title")
                                .withRequired(true),
                        InputParameter.stringParameter("body")
                                .withField(() -> {
                                    TextArea<String> textArea = uiComponents.create(TextArea.TYPE_STRING);
                                    textArea.setCaption("Body");
                                    textArea.setRequired(true);
                                    textArea.setWidthFull();

                                    return textArea;
                                })
                )
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        String title = closeEvent.getValue("title");
                        String body = closeEvent.getValue("body");

                        Set<User> selected = usersTable.getSelected();
                        Collection<User> users = selected.isEmpty()
                                ? usersDc.getItems()
                                : selected;

                        doSendEmail(title, body, users);
                    }
                })
                .show();
    }*/



    @Autowired
    private BackgroundWorker backgroundWorker;

    private void doSendEmail(String title, String body, Collection<User> users) {
        BackgroundTask<Integer, Void> task = new EmailTask(title, body, users);

        dialogs.createBackgroundWorkDialog(this, task)
                .withCaption("Sending reminder emails")
                .withMessage("Please wait while emails are being sent")
                .withTotal(users.size())
                .withShowProgressInPercentage(true)
                .withCancelAllowed(true)
                .show();

        /*backgroundWorker.handle(task);*/
    }

    private class EmailTask extends BackgroundTask<Integer, Void> {

        private final String title;
        private final String body;
        private final Collection<User> users;

        public EmailTask(String title, String body, Collection<User> users) {
            super(10, TimeUnit.MINUTES, UserBrowse.this);

            this.title = title;
            this.body = body;
            this.users = users;
        }

        @Override
        public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            int i = 0;
            for (User user : users) {
                if (taskLifeCycle.isCancelled()) {
                    break;
                }

                // Imitate time-consuming work
                TimeUnit.SECONDS.sleep(2);

                i++;
                taskLifeCycle.publish(i);
            }

            return null;
        }

        @Override
        public void done(Void result) {
            notifications.create()
                    .withCaption("Email has been sent")
                    .withType(Notifications.NotificationType.TRAY)
                    .show();
        }
    }
}