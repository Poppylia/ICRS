package com.shencangblue.design.icrs.model;

import javax.persistence.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity//加入这个注解，Demo就会进行持久化了，在这里没有对@Table进行配置，请自行配置。
@Table(name = "class_room")
public class ClassRoom {
    @Id
    @GeneratedValue
    private int roomId;//房间iD
    private int roomNum;//房间号码
    private String roomName;//房间名
    private int rows;//行数
    private int cols;//列数
    private int capacity;//容量
    private int status;//状态
    private String position;//位置
    private String description;// 描述
    @Transient
    private int remainSeats;//剩余座位



    public ClassRoom(){}

    public ClassRoom(int roomId,int roomNum,String roomName,int capacity,int status,String description,String position){
        this.roomId= roomId;
        this.roomNum=roomNum;
        this.roomName =roomName;
        this.capacity =capacity;
        this.status = status;
        this.description= description;
        this.position= position;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getRemainSeats() {
        return remainSeats;
    }

    public void setRemainSeats(int remainSeats) {
        this.remainSeats = remainSeats;
    }

    @PostLoad
    public void setRemainSeats() {
        this.remainSeats = this.capacity;
    }

    public static void main(String[] args) {
        Clock baseclock = Clock.fixed(Instant.parse("1970-01-31T10:15:30.00Z"),
                ZoneId.of("Asia/Calcutta"));
        System.out.println("Instant of Base class "
                + baseclock.instant());
        Clock clock3 = Clock.tick(baseclock,
                Duration.ofDays(5));
        System.out.println("Instant of Clock2 when duration"
                + " = 5 days is "
                + clock3.instant());
    }
}
