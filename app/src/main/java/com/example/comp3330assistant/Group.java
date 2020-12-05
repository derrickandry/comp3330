package com.example.comp3330assistant;

import java.util.HashMap;
import java.util.Map;



public class Group {
    private String APK;
    private String AppName;
    private String Description;
    private String Logo;
    private String Video;
    private String Details;
    private String Source;
    private Map<String, String> Members;
    private String groupNumber;

    public Group(){

    }
    public Group(String APK, String AppName, String Description, String Logo, String Video, String Details, String Source, Map<String, String> Members){
        this.APK = APK;
        this.AppName = AppName;
        this.Description = Description;
        this.Logo = Logo;
        this.Video = Video;
        this.Members = Members;
        this.Details = Details;
        this.Source = Source;
    }
    public Group(String APK, String AppName, String Description, String Logo, String Video, String Details, String Source, String Members){
        this.APK = APK;
        this.AppName = AppName;
        this.Description = Description;
        this.Logo = Logo;
        this.Video = Video;
        this.Details = Details;
        this.Source = Source;
        String[] members = Members.split(",");
        Map<String, String> memberList = new HashMap<>();
        for(int i = 1; i < members.length+1; i++){
            memberList.put("m"+i, members[i-1]);
        }
        this.Members = memberList;
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

    public Map<String, String> getMembers() {
        return Members;
    }

    public void setMembers(Map<String, String> Members) {
        this.Members = Members;
    }
    public void setGroupNumber(String groupNumber){
        this.groupNumber = groupNumber;
    }
    public String getGroupNumber(){
        return this.groupNumber;
    }
    public String AllMembers(){
        String everyone = "";
        for(String member: Members.values()){
            everyone += member +", ";
        }

        return everyone.substring(0, everyone.length()-2);
    }
    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }
    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }


}
