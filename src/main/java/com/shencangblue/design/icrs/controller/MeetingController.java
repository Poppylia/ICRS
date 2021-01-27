package com.shencangblue.design.icrs.controller;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.shencangblue.design.icrs.model.ClassRoom;
import com.shencangblue.design.icrs.model.Meeting;
import com.shencangblue.design.icrs.model.User;
import com.shencangblue.design.icrs.result.Result;
import com.shencangblue.design.icrs.result.ResultFactory;
import com.shencangblue.design.icrs.service.ClassRoomService;
import com.shencangblue.design.icrs.service.MeetingService;
import com.shencangblue.design.icrs.service.UserService;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class MeetingController {
    @Resource
    MeetingService meetingService;
    @Resource
    ClassRoomService classRoomService;
    Timestamp nowTime;
    Timestamp tomTime;

    /**
     * 保存或者修改会议活动
     *
     * @param meeting 活动与会议
     * @return 封装的提示消息
     */
    @RequestMapping("/meeting/save")
    public Result save(@RequestBody Meeting meeting) {
        meetingService.save(meeting);
        if (meetingService.getById(meeting.getMeetingId()) != null) {
            return ResultFactory.buildSuccessResult("添加成功");
        }
        return ResultFactory.buildFailResult("添加失败");
    }

    /**
     * 删除指定活动
     *
     * @param meeting 要删除指定活动
     * @return 封装的封装消息
     */
    @RequestMapping("/meeting/delete")
    public Result delete(@RequestBody Meeting meeting) {
        System.out.println(meeting.getMeetingId());
        meetingService.delete(meeting.getMeetingId());
        return ResultFactory.buildSuccessResult("success");
    }

    /**
     * 查找所有会议
     *
     * @return 封装好的所有会议信息
     */
    @RequestMapping("/meeting")
    public Result getAll() {
        return ResultFactory.buildSuccessResult(meetingService.getAll());
    }

    /**
     * 获取活动数量
     *
     * @return 封装好的活动数量
     */
    @RequestMapping("/meeting/count")
    public Result getAllCount() {
        return ResultFactory.buildSuccessResult(meetingService.CountByAllMeeting());
    }

    /**
     * 获取指定用户的活动
     *
     * @param requestUser 要查找的用户
     * @return 封装好活动信息列表
     */
    @RequestMapping("/meeting/get/user")
    public Result getAllMeetingByUsername(@RequestBody User requestUser) {
        return ResultFactory.buildSuccessResult(meetingService.findAllByStuId(requestUser.getUsername()));
    }

    /**
     * 获取管理员的活动
     *
     * @return 封装好的管理员活动
     */
    @RequestMapping("/meeting/get/admin")
    public Result getAllMeetingByAdmin() {
        return ResultFactory.buildSuccessResult(meetingService.findAllByStuId("admin"));
    }

    /**
     * 获取已预定的座位
     *
     * @param meeting 活动与会议
     * @return 座位信息
     */
    @RequestMapping("/meeting/get/seat")
    public Result getSeat(@RequestBody Meeting meeting) {
        return ResultFactory.buildSuccessResult(meetingService.findAllByMeeting(meeting));
    }

    /**
     * 查询用户可用的活动
     *
     * @param requestUser 要查询的用户
     * @return 封装好的用户
     */
    @RequestMapping("/meeting/get/user_used")
    public Result getAllMeetingByUsernameUsable(@RequestBody User requestUser) {
        return ResultFactory.buildSuccessResult(meetingService.findAllByStuIdUsable(requestUser.getUsername()));
    }

    /**
     * 取消活动
     *
     * @param meeting 要取消的活动
     * @return 封装好的取消提示
     */
    @RequestMapping("/meeting/cancel")
    public Result cancelMeeting(@RequestBody Meeting meeting) {
        Meeting meeting_s = meetingService.getById(meeting.getMeetingId());
        if(meeting_s.getStatus() != 1 || meeting.getEndTime().before(new Date())) {
            return ResultFactory.buildFailResult("预定已过期或已取消");
        }
        meeting_s.setStatus(0);
        meeting_s.setCanceledTime(meeting.getCanceledTime());
        meeting_s.setCanceledReason(meeting.getCanceledReason());
        meetingService.save(meeting_s);
        return ResultFactory.buildSuccessResult("取消成功");
    }

    /**
     * 查询用户取消的活动
     *
     * @param requestUser 要查询的用户
     * @return 封装好的活动列表
     */
    @RequestMapping("/meeting/get/user_cancel")
    public Result getAllMeetingByUsernameCanceled(@RequestBody User requestUser) {
        return ResultFactory.buildSuccessResult(meetingService.findAllByStuIdCancel(requestUser.getUsername()));
    }

    /**
     * 查询用户超时活动
     *
     * @param requestUser 要查询的用户
     * @return 封装好的查询结果
     */
    @RequestMapping("/meeting/get/user_timeout")
    public Result getAllMeetingByUsernameTimeout(@RequestBody User requestUser) {
        return ResultFactory.buildSuccessResult(meetingService.findAllByStuIdTimeout(requestUser.getUsername()));
    }

    /**
     * 查询所有可用的活动数量
     *
     * @return 封装好的可用活动数量
     */
    @RequestMapping("/meeting/get/count/classify/usable")
    public Result getAllMeetingCountClassifyUsable() {
        return ResultFactory.buildSuccessResult(meetingService.CountByAllMeetingUsable());
    }

    /**
     * 查询所有取消和超时的活动
     *
     * @return 封装好的超时和取消活动数量
     */
    @RequestMapping("/meeting/get/count/classify/c&t")
    public Result getAllMeetingCountClassifyCancelAndTimeout() {
        return ResultFactory.buildSuccessResult(meetingService.CountByAllMeetingCancelAndTimeout());
    }

    /**
     * 查询当前日期所有教室预约情况
     *
     * @return 预约情况
     */
    @RequestMapping("/queryReservationOfCurrentDate")
    public Result QueryReservationOfCurrentDate() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Iterable<Meeting> meetings = meetingService.findAllByStartTimeBetweenAndStatusGreaterThan(Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay), 0);

        Table<Integer, Integer, Integer> table = HashBasedTable.create();
        for (Meeting meeting : meetings) {
            int start = meeting.getStartTime().toLocalDateTime().plusMinutes(30).getHour();
            int end = meeting.getEndTime().toLocalDateTime().minusMinutes(30).getHour();
            for (int i = start; i <= end; i++) {
                Integer count = table.get(i, meeting.getRoomId());
                count = count != null? count + 1 : 1;
                table.put(i, meeting.getRoomId(), count);
            }
        }
        List<int[]> list = table.cellSet().stream().map(cell -> new int[]{cell.getRowKey(), cell.getColumnKey(), cell.getValue()}).collect(Collectors.toList());

        return ResultFactory.buildSuccessResult(list);
    }

    /**
     * 查询活动预约的状态
     *
     * @return
     */
    @RequestMapping("/queryReservationOfCurrentDateRoom")
    public Result QueryReservationOfCurrentDateRoom() {
        List<String> list = new ArrayList<>();
        nowTime = new Timestamp(new Date().getTime());
        tomTime = new Timestamp(new Date().getTime());
        nowTime.setHours(0);
        nowTime.setSeconds(0);
        nowTime.setMinutes(0);
        nowTime.setNanos(0);
        tomTime.setHours(0);
        tomTime.setSeconds(0);
        tomTime.setMinutes(0);
        tomTime.setNanos(0);
        tomTime.setDate(tomTime.getDate() + 1);
        for (Meeting meeting : meetingService.findAllByStartTimeBetweenAndStatusGreaterThan(nowTime, tomTime, 0)) {

            list.add(meeting.getRoomName());
            ;
        }
        return ResultFactory.buildSuccessResult(list);
    }

    /**
     * 预约签到
     */
    @RequestMapping("/meeting/checkIn")
    public Result checkMeetIn(@RequestBody Meeting meeting) {
        Meeting m = meetingService.getById(meeting.getMeetingId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);
        if(m.getStartTime().after(calendar.getTime()) || m.getEndTime().before(new Date())) {
            return ResultFactory.buildFailResult("当前不在预约时间内");
        }
        m.setStatus(2);
        meetingService.save(m);
        return ResultFactory.buildSuccessResult("签到成功");
    }


    /**
     * 查询活动是否结束
     * @return 封装好的活动状态提升
     */
    @RequestMapping("/checkMeetBegin")
    public Result checkMeetBegin() {
        Iterable<Meeting> meetings = meetingService.findAllByStartTimeAfterAndEndTimeBefore(nowTime, nowTime);
        nowTime = new Timestamp(new Date().getTime());
        for (Meeting meeting : meetings) {
            meeting.setStatus(2);
        }
        meetingService.saveAll(meetings);
        return ResultFactory.buildSuccessResult("更新状态成功");
    }

    /**
     * 查询活动是否过期
     * @return 返回更新状态
     */
    @RequestMapping("/checkMeetTimeout")
    public Result checkMeetTimeout() {
        nowTime = new Timestamp(new Date().getTime());
        Iterable<Meeting> meetings = meetingService.findAllByEndTimeBefore(nowTime);
        for (Meeting meeting : meetings) {
            meeting.setStatus(-1);
            System.out.println(meeting.getMeetingId() + " " + meeting.getStatus());
        }
        meetingService.saveAll(meetings);
        return ResultFactory.buildSuccessResult("更新状态成功");
    }

    /**
     * 查询七天内所有用户会议所有教室预约情况
     *
     * @return 预约情况
     */
    @RequestMapping("/querySevenDayMeetOfUser")
    public Result querySevenDayMeetOfUser(@RequestBody User requestUser) {
        return ResultFactory.buildSuccessResult(meetingService.querySevenDayMeetOfUser(requestUser.getUsername()));
    }

    /**
     * 查询已预约座位统计和违规统计
     *
     * @return 预约情况
     */
    @RequestMapping("/queryReservationStatsOfRecently")
    public Result queryReservationStatsOfRecently(@RequestBody Meeting meeting) {
        Timestamp startTime = meeting.getStartTime();
        Timestamp endTime = meeting.getEndTime();
        if(startTime == null || endTime == null) {
            LocalDateTime nowTime = LocalDateTime.now();
            startTime = Timestamp.valueOf(nowTime.minusDays(7));
            endTime = Timestamp.valueOf(nowTime);
        }
        List<Integer> status = new ArrayList<>(2);
        status.add(meeting.getStatus());
        if(meeting.getStatus() == 1) {
            status.add(2);
        }
        Iterable<Meeting> iterable= meetingService.queryReservationStats(startTime, endTime, status);
        Map<String, Map<Integer, Long>> date2RoomId2Count = StreamSupport.stream(iterable.spliterator(), true)
                .collect(Collectors.groupingBy(m -> m.getStartTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                Collectors.groupingBy(Meeting::getRoomId, Collectors.counting())));

        LocalDate endDay = endTime.toLocalDateTime().toLocalDate();
        List<String> dayList = new ArrayList<>();
        long between = ChronoUnit.DAYS.between(startTime.toInstant(), endTime.toInstant());
        for (long i = between; i >= 0 ; i++) {
            dayList.add(endDay.minusMonths(between).toString());
        }

        List<List> table = getTable(dayList, date2RoomId2Count);
        return ResultFactory.buildSuccessResult(table);
    }

    /**
     * 查询已预约座位统计和违规统计
     *
     * @return 预约情况
     */
    @RequestMapping("/queryReservationStatsOfYear")
    public Result queryReservationStatsOfYear(@RequestParam Integer year, @RequestParam List<Integer> status) {
        LocalDate now = LocalDate.now();
        if(year == null || year < 2000) {
           year = now.getYear();
        }
        Timestamp startTime = Timestamp.valueOf(LocalDate.ofYearDay(year, 1).atTime(LocalTime.MIN));
        Timestamp endTime = Timestamp.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX));
        if(now.getYear() == year) {
            endTime = Timestamp.valueOf(endTime.toLocalDateTime().withMonth(now.getMonthValue()));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM");
        Iterable<Meeting> iterable= meetingService.queryReservationStats(startTime, endTime, status);
        Map<String, Map<Integer, Long>> date2RoomId2Count = StreamSupport.stream(iterable.spliterator(), true)
                .collect(Collectors.groupingBy(m -> m.getStartTime().toLocalDateTime().format(formatter),
                        Collectors.groupingBy(Meeting::getRoomId, Collectors.counting())));

        LocalDateTime endMonth = endTime.toLocalDateTime();
        List<String> monthList = new ArrayList<>();
        long between = ChronoUnit.MONTHS.between(startTime.toInstant(), endTime.toInstant());
        for (long i = between; i >= 0 ; i++) {
            monthList.add(endMonth.minusMonths(between).format(formatter));
        }

        List<List> table = getTable(monthList, date2RoomId2Count);
        return ResultFactory.buildSuccessResult(table);
    }

    private List<List> getTable(List<String> list, Map<String, Map<Integer, Long>> date2RoomId2Count) {
        List<ClassRoom> classRooms = new LinkedList<>();
        classRoomService.getAll().forEach(classRooms::add);

        List<List> table = new ArrayList<>(list.size());
        for (String date : list) {
            List<Object> row= new ArrayList<>(classRooms.size() + 1);
            row.add(date);
            Map<Integer, Long> roomId2Count = date2RoomId2Count.get(date);
            for (ClassRoom room : classRooms) {
                Long count = roomId2Count.getOrDefault(room.getRoomId(), 0L);
                row.add(count);
            }
            table.add(row);
        }
        List<String> head = classRooms.stream().map(ClassRoom::getRoomName).collect(Collectors.toList());
        head.add(0, "");
        table.add(0, head);
        return table;
    }

    /**
     * 依照会议时间是否冲突-重新封装模式
     *
     * @return 是否冲突
     */
    @RequestMapping("/checkTimeConflict")
    public Result checkTimeConflict(@RequestBody Meeting meeting) {
        return ResultFactory.buildSuccessResult(meetingService.checkTimeConflict(meeting));
    }



}

