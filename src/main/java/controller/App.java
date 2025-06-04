package controller;

import model.User;
import view.menu.GameMenu;

import java.util.ArrayList;

public class App {
    private static User currentUser;
    private static ArrayList<User> allUsers;
    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ArrayList<User> allusers) {
        allUsers = allusers;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }
    public static void run(){
        DBController.loadCurrentUser();
        DBController.loadUsers();
        if (App.getCurrentUser() != null)
            App.setCurrentUser(UserController.getUserByUsername(App.currentUser.getUsername()));
        GameMenu.playMusic();
    }
    public static boolean isStayLoggedIn() {
        return currentUser != null;
    }
}
