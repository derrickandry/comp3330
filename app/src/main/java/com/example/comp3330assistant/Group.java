package com.example.comp3330assistant;

import java.util.ArrayList;
import java.util.HashMap;


public class Group {
    String APK, AppName, Description, Logo, Video;
    HashMap<String, String> Members;
    private String groupNumber;

    public Group(){

    }
    public Group(String APK, String AppName, String Description, String Logo, String Video, HashMap<String, String> Members){
        this.APK = APK;
        this.AppName = AppName;
        this.Description = Description;
        this.Logo = Logo;
        this.Video = Video;
        this.Members = Members;
    }

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String Video) {
        this.Video = Video;
    }

    public HashMap<String, String> getMembers() {
        return Members;
    }

    public void setMembers(HashMap<String, String> Members) {
        this.Members = Members;
    }
    public void setGroupNumber(String groupNumber){
        this.groupNumber = groupNumber;
    }
    public String getGroupNumber(){
        return this.groupNumber;
    }
    public String getAllMembers(){
        String everyone = "";
        for(String member: Members.values()){
            everyone += member +", ";
        }

        return everyone.substring(0, everyone.length()-2);
    }
}
