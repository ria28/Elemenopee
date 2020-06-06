package org.wazir.build.elemenophee.Student.Lecture.Subject;

import java.util.ArrayList;

public class ClasObjTea {
        private String title, extSub;
        private ArrayList<SubObj> subs;


        public ClasObjTea() {
        }

        public ClasObjTea(String title, ArrayList<SubObj> subs) {
            this.title = title;
            this.subs = subs;
        }

        public ClasObjTea(String title, String extSub, ArrayList<SubObj> subs) {
            this.title = title;
            this.extSub = extSub;
            this.subs = subs;
        }

        public String getExtSub() {
            return extSub;
        }

        public void setExtSub(String extSub) {
            this.extSub = extSub;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public ArrayList<SubObj> getSubs() {
            return subs;
        }

        public void setSubs(ArrayList<SubObj> subs) {
            this.subs = subs;
        }
}
