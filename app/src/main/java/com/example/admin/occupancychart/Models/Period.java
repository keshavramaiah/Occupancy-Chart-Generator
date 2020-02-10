package com.example.admin.occupancychart.Models;

public class Period {
    private String TeacherName,Room,Time,Title;

    public Period() {
    }

    public Period(String room, String time, String title) {
        //TeacherName = teacherName;
        Room = room;
        Time = time;
        Title = title;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
