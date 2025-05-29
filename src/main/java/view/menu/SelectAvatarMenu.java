package view.menu;

import controller.App;
import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Paths;

import java.io.File;
import java.net.URL;

public class SelectAvatarMenu extends Application {
    private static boolean isChangingAvatarMenuActive = false;
    @FXML
    private BorderPane avatarPane;

    @FXML
    private ImageView avatar1, avatar2, avatar3, avatar4, avatar5, avatar6, avatar7, avatar8;

    @FXML
    private ImageView selectedAvatar;

    @FXML
    private Button chooseAvatarFromFileButton, randomAvatarButton, registerButton;

    public static boolean isIsChangingAvatarMenuActive() {
        return isChangingAvatarMenuActive;
    }

    public static void setIsChangingAvatarMenuActive(boolean isChangingAvatarMenuActive) {
        SelectAvatarMenu.isChangingAvatarMenuActive = isChangingAvatarMenuActive;
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL selectAvatarMenuFXMLUrl = SelectAvatarMenu.class.getResource
                (Paths.SELECT_AVATAR_MENU_FXML_FILE.getPath());
        BorderPane borderPane = FXMLLoader.load(selectAvatarMenuFXMLUrl);
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        if (isChangingAvatarMenuActive) {
            ImageView imageView = new ImageView(new Image(App.getCurrentUser().getAvatarFilePath()));
            imageView.setTranslateY(675);
            imageView.setTranslateX(175);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            borderPane.getChildren().add(imageView);
        }
        Scene selectAvatarMenuScene = new Scene(borderPane);
        stage.setScene(selectAvatarMenuScene);
        stage.show();
    }

    public void back(MouseEvent mouseEvent) {
        if (isChangingAvatarMenuActive) {
            try {
                SelectAvatarMenu.setIsChangingAvatarMenuActive(false);
                new ProfileMenu().start(LoginMenu.stageOfProgram);
            } catch (Exception e) {
                System.out.println("an error occurred");
            }
        } else {
            try {
                UserController.setTemporaryAvatarAddress(null);
                new RegisterMenu().start(LoginMenu.stageOfProgram);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void Register(MouseEvent mouseEvent) {
        if (UserController.getTemporaryAvatarAddress() == null) {
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Register failed!" ,
                    "Register was not successful!" , "You didn't choose any avatar!");
            return;
        }
        if (isChangingAvatarMenuActive) {
            UserController.changeAvatar();
            GameViewController.alertShowing(Alert.AlertType.INFORMATION ,
                    "Change information was successful!",
                    "Avatar changed successfully!" , "Avatar was changed!");
            try {
                SelectAvatarMenu.setIsChangingAvatarMenuActive(true);
                new ProfileMenu().start(LoginMenu.stageOfProgram);
            }catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            UserController.createUser();
            GameViewController.alertShowing(Alert.AlertType.INFORMATION , "User creation was successful!",
                    "User was created successfully!" , " User was created!");
            try {
                new MainMenu().start(LoginMenu.stageOfProgram);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

//    public void initialize() {
//        if (isChangingAvatarMenuActive) {
//            registerButton.setText("Change");
//            // changing text of Register button to "Change" for changing Avatar menu
//        }
//        randomAvatarButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                UserController.chooseRandomAvatar();
//                selectedAvatar.setImage(new Image(UserController.getTemporaryAvatarAddress()));
//            }
//        });
//        chooseAvatarFromFileButton.setOnDragOver(event -> {
//            if (event.getDragboard().hasFiles()) {
//                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
//            }
//            event.consume();
//        });
//
//        chooseAvatarFromFileButton.setOnDragDropped(event -> {
//            var db = event.getDragboard();
//            boolean success = false;
//            if (db.hasFiles()) {
//                File file = db.getFiles().get(0);
//                try {
//                    String filePath = file.toURI().toString();
//                    Image img = new Image(filePath);
//                    selectedAvatar.setImage(img);
//                    UserController.setTemporaryAvatarAddress(filePath);
//                    success = true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    GameViewController.alertShowing(Alert.AlertType.ERROR,
//                            "Invalid Image",
//                            "Failed to load the dropped image",
//                            e.getMessage());
//                }
//            }
//            event.setDropCompleted(success);
//            event.consume();
//        });
//
//
//        chooseAvatarFromFileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                String addressBeforeChangeAddress = UserController.getTemporaryAvatarAddress();
//                new FileChoosingMenu().start(LoginMenu.stageOfProgram);
//                if (addressBeforeChangeAddress == null ||
//                        !addressBeforeChangeAddress.equals(UserController.getTemporaryAvatarAddress())) {
//                    try {
//                        selectedAvatar.setImage(new Image(UserController.getTemporaryAvatarAddress()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        chooseAvatarFromTemplatesEventHandling(avatar1 , 1);
//        chooseAvatarFromTemplatesEventHandling(avatar2 , 2);
//        chooseAvatarFromTemplatesEventHandling(avatar3 , 3);
//        chooseAvatarFromTemplatesEventHandling(avatar4 , 4);
//        chooseAvatarFromTemplatesEventHandling(avatar5 , 5);
//        chooseAvatarFromTemplatesEventHandling(avatar6 , 6);
//        chooseAvatarFromTemplatesEventHandling(avatar7 , 7);
//        chooseAvatarFromTemplatesEventHandling(avatar8 , 8);
//    }

    @FXML
    public void initialize() {
        if (isChangingAvatarMenuActive) {
            registerButton.setText("Change");
            // changing text of Register button to "Change" for changing Avatar menu
        }

        chooseAvatarFromFileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String addressBeforeChangeAddress = UserController.getTemporaryAvatarAddress();
                new FileChoosingMenu().start(LoginMenu.stageOfProgram);
                if (addressBeforeChangeAddress == null ||
                        !addressBeforeChangeAddress.equals(UserController.getTemporaryAvatarAddress())) {
                    try {
                        selectedAvatar.setImage(new Image(UserController.getTemporaryAvatarAddress()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        chooseAvatarFromTemplatesEventHandling(avatar1 , 1);
        chooseAvatarFromTemplatesEventHandling(avatar2 , 2);
        chooseAvatarFromTemplatesEventHandling(avatar3 , 3);
        chooseAvatarFromTemplatesEventHandling(avatar4 , 4);
        chooseAvatarFromTemplatesEventHandling(avatar5 , 5);
        chooseAvatarFromTemplatesEventHandling(avatar6 , 6);
        chooseAvatarFromTemplatesEventHandling(avatar7 , 7);
        chooseAvatarFromTemplatesEventHandling(avatar8 , 8);
    }

    private void chooseAvatarFromTemplatesEventHandling(ImageView avatar , int number) {
        avatarPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        avatarPane.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                try {
                    String filePath = file.toURI().toString();
                    Image img = new Image(filePath);
                    selectedAvatar.setImage(img);
                    UserController.setTemporaryAvatarAddress(filePath);
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    GameViewController.alertShowing(Alert.AlertType.ERROR,
                            "Invalid Image",
                            "Failed to load the dropped image",
                            e.getMessage());
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        avatar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedAvatar.setImage(new Image(UserController.class.getResource
                        ("/images/Avatars/").toString()
                        + number + ".png"));
                UserController.choosingAvatarFromTemplates(number);
            }
        });
    }
}
