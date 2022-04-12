package com.zeyalychat.com.utils;

public class URLHelper {

    public static String baseURL = "https://api.classmate.zeyaly.com/";

    public static String createaccount = baseURL + "iam/createaccount";
    public static String auth = baseURL + "iam/auth";
    public static String messageList = baseURL + "zmsg/messages";
    public static String contactList = baseURL + "zmsg/search";
    public static String messageConvFromSearch = baseURL + "zmsg/messages?";
    public static String conv = baseURL + "zmsg/messages/";
    public static String timeTable = baseURL + "ttl/timetable?";
    public static String syllabuslist = baseURL + "syb/syllabus";
    public static String getuserinfo = baseURL + "iam/current_user_info";
    public static String syllabusDetail = baseURL + "syb/syllabus?syllabus_id=";
    public static String homwwork = baseURL + "hmw/homework";
    public static String meeting = baseURL + "met/meeting";
    public static String exam = baseURL + "exm/exam";
    public static String SMSGET = baseURL + "sms/sms?to=2&to_type=";
    public static String examType = baseURL + "exm/examtype";
    public static String holiday = baseURL + "atd/holiday";
    public static String feetype = baseURL + "fee/type";
    public static String fee = baseURL + "fee/fees";
    public static String tutorial = baseURL + "elr/tutorial";
    public static String feeSublist = baseURL + "fee/fees?id=";
    public static String timeline = baseURL + "tln/timeline";
    public static String comments = baseURL + "tln/comments?id=";
    public static String like = baseURL + "tln/likes?id=";
    public static String homeworktype = baseURL + "hmw/homeworktype";
    public static String homeworkstatuscompleted = baseURL + "hmw/homework/status?status=completed&id=";
    public static String examstatuscompleted = baseURL + "exm/exam/status?status=completed&id=";
    public static String homeworkstatuspending = baseURL + "hmw/homework/status?status=pending&id=";
    public static String examstatuspending = baseURL + "exm/exam/status?status=pending&id=";
    public static String homeworkupload = baseURL + "hmw/homework/upload?id=";
    public static String examupload = baseURL + "exm/exam/upload?id=";
    public static String Leavetype = baseURL + "atd/type";

    public static String AttendanceList = baseURL + "atd/attendance/student?";
    public static String AttendanceCreate = baseURL + "atd/attendance/student";
    public static String MeetingCreate = baseURL + "met/meeting";
    public static String StdList = baseURL + "usr/student?section_id=";

    public static String profile =  baseURL+"iam/profile_picture?id=";

    public static String levaeListStd = baseURL + "atd/leave/student";
    public static String LeaveListStaff = baseURL + "atd/leave/staff";
    public static String section = baseURL + "mdl/search-grade-section";
    public static String sectionType = baseURL + "sms/sender?to_type=2";
    public static String SMS = baseURL + "sms/sms";
    public static String subject = baseURL + "mdl/search/subjects/all?search=true&grade_id=grade_id";



}
