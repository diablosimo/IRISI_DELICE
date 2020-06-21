package sample.util;
import sample.bean.Admin;
import sample.bean.Client;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static List<SessionItem> myMap = new ArrayList<>();


    public static Object getAttribut(String name) {
        for (int i = 0; i < myMap.size(); i++) {
            SessionItem sessionItem = myMap.get(i);
            if (sessionItem.getKey().equals(name)) {
                return sessionItem.getObject();
            }
        }
        return null;
    }

    public static String getConnectedAdmin(){
        Admin u=((Admin)Session.getAttribut("connectedAdmin"));
        if(u==null) return null;
        else return u.getNom()+" "+u.getPrenom()+" ("+u.getLogin()+")";
    }

    public static String getConnectedClient(){
        Client u=((Client)Session.getAttribut("connectedUser"));
        if(u==null) return null;
        else return u.getNom()+" "+u.getPrenom()+" ("+u.getEmail()+")";
    }

    public static Client getConClient(){
        Client u=((Client)Session.getAttribut("connectedUser"));
        if(u==null) return null;
        else return u;
    }
    public static Admin getConAdmin(){
        Admin u=((Admin)Session.getAttribut("connectedAdmin"));
        if(u==null) return null;
        else return u;
    }

    public static int updateAttribute(Object obj, String name) {
        int index = indexOfAttribut(name);
        if (index == -1) {
            createAtrribute(obj,name );
            return 1;
        } else {
            myMap.get(index).setObject(obj);
            return 2;
        }
    }

    public static int delete(String name) {
        int index = indexOfAttribut(name);
        if (index != -1) {
            myMap.remove(index);
            return 1;
        }
        return -1;

    }

    public static void clear() {
        myMap.clear();
    }

    public static void createAtrribute(Object obj,String name ) {
        SessionItem sessionItem = new SessionItem();
        sessionItem.setKey(name);
        sessionItem.setObject(obj);
        myMap.add(sessionItem);
    }

    private static int indexOfAttribut(String name) {
        for (int i = 0; i < myMap.size(); i++) {
            SessionItem sessionItem = myMap.get(i);
            if (sessionItem.getKey().equals(name)) {
                return i;
            }
        }
        return -1;

    }


}
