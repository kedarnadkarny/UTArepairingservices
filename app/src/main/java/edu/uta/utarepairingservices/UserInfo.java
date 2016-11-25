package edu.uta.utarepairingservices;

// This class will have all the getter setter methods required to use in other Activities
// For Ex. uta_net_id is a field which will be used most of the time to perform CRUD opertions

import android.app.Application;

public class UserInfo extends Application {

    private static String uta_net_id;
    private static String RoleId;

    public UserInfo() {
    }

    public UserInfo(String uta_net_id) {
        this.uta_net_id = uta_net_id;
    }

    public String getUta_net_id() {
        return uta_net_id;
    }

    public void setUta_net_id(String uta_net_id) {
        this.uta_net_id = uta_net_id;
    }

    public static String getRoleId() {
        return RoleId;
    }

    public static void setRoleId(String roleId) {
        RoleId = roleId;
    }

}
