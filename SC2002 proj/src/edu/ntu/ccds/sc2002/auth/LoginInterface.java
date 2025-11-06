package edu.ntu.ccds.sc2002.auth;

import edu.ntu.ccds.sc2002.model.*;
import java.util.*;

public interface LoginInterface {
    boolean login(String idOrEmail, String password);
    void logout();
    boolean changePassword(String idOrEmail, String oldPwd, String newPwd);
    Optional<User> currentUser();
}