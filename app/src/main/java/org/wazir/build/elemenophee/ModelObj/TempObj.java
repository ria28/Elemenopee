package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class TempObj {
    ArrayList<String> Contacts;

    public TempObj() {
        this.Contacts = new ArrayList<>();
    }

    public ArrayList<String> getContacts() {
        return Contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        Contacts = contacts;
    }
}
