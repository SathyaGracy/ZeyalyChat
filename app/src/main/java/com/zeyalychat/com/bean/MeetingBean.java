package com.zeyalychat.com.bean;

public class MeetingBean {
    String end_time;
    String id;
    String meeting_id;
    String meeting_url;
    String start_time;
    String title;
    String Section_id;

    public String getSection_id() {
        return Section_id;
    }

    public void setSection_id(String section_id) {
        Section_id = section_id;
    }

    public String getSection_name() {
        return Section_name;
    }

    public void setSection_name(String section_name) {
        Section_name = section_name;
    }

    String Section_name;


    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_url() {
        return meeting_url;
    }

    public void setMeeting_url(String meeting_url) {
        this.meeting_url = meeting_url;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
