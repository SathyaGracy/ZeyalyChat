package com.zeyalychat.com.bean;

public class SectionBean {
    int grade_id;
    String grade_name;
    int section_id;
    String section_name;

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    Boolean isSelect;

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public Boolean getSection_visibility() {
        return section_visibility;
    }

    public void setSection_visibility(Boolean section_visibility) {
        this.section_visibility = section_visibility;
    }

    Boolean section_visibility;

}
