package com.shencangblue.design.icrs.service;

import com.shencangblue.design.icrs.dao.MeetingDao;
import com.shencangblue.design.icrs.model.Meeting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Resource
    MeetingDao meetingDao;

    /**
     * 保存 所有活动
     *
     * @param meetings 要保存的活动列表
     */
    @Transactional
    public void saveAll(Iterable<Meeting> meetings) {
        meetingDao.saveAll(meetings);
    }

    /**
     * 保存或者新建活动
     *
     * @param meeting 活动
     * @return 保存成功
     */
    @Transactional
    public boolean save(Meeting meeting) {

        meetingDao.save(meeting);
        return true;
    }

    /**
     * 删除指定的活动
     *
     * @param id 要删除的活动ID
     */
    @Transactional
    public void delete(Long id) {
        meetingDao.deleteById(id);
    }

    /**
     * 通过ID获取活动
     *
     * @param id 要获取的ID
     * @return 对应的活动
     */
    @Transactional
    public Meeting getById(Long id) {
        return meetingDao.findById(id).orElse(null);
    }

    /**
     * 获取所有的活动
     *
     * @return 所有的活动列表
     */
    @Transactional
    public Iterable<Meeting> getAll() {
        return meetingDao.findAll();
    }

    /**
     * 通过房间号、开始结束时间获取占座信息
     *
     * @param meeting 预定信息
     * @return 对应的活动列表
     */
    @Transactional
    public Iterable<Meeting> findAllByMeeting(Meeting meeting) {
        return meetingDao.findAllByRoomIdAndStartTimeBeforeAndEndTimeAfterAndStatusGreaterThan(meeting.getRoomId(), meeting.getEndTime(), meeting.getStartTime(), 0);
    }

    /**
     * 通过房间号获取活动
     *
     * @param id 房间号
     * @return 对应的活动列表
     */
    @Transactional
    public Iterable<Meeting> findAllByRoomId(long id) {
        return meetingDao.findAllByRoomId(id);
    }

    /**
     * 通过用户名获取活动
     *
     * @param username 用户名
     * @return 对应活动列表
     */
    @Transactional
    public Iterable<Meeting> findAllByStuId(String username) {
        return meetingDao.findAllByStuName(username);
    }

    /**
     * 获取用户可用活动
     *
     * @param username 用户
     * @return 对应活动列表
     */
    @Transactional
    public Iterable<Meeting> findAllByStuIdUsable(String username) {
        return meetingDao.findAllByStuNameAndStatus(username, 1);
    }

    /**
     * 获取用户取消的活动列表
     *
     * @param username 用户名
     * @return 获取用户取消的活动
     */
    @Transactional
    public Iterable<Meeting> findAllByStuIdCancel(String username) {
        return meetingDao.findAllByStuNameAndCanceledTimeNotNull(username);
    }

    /**
     * 获取用户超时的活动列表
     *
     * @param username 用户名
     * @return 获取用户超时的活动
     */
    @Transactional
    public Iterable<Meeting> findAllByStuIdTimeout(String username) {
        return meetingDao.findAllByStuNameAndStatus(username, -1);
    }

    /**
     * 获取活动数量
     *
     * @return 活动数量
     */
    @Transactional
    public int CountByAllMeeting() {
        Iterator<?> it = meetingDao.findAll().iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * 获取可用的活动数量
     *
     * @return 可用的活动数量
     */
    @Transactional
    public int CountByAllMeetingUsable() {
        return meetingDao.countByStatus(1);
    }

    /**
     * 获取不可用的活动数量
     *
     * @return 不可用的活动数量
     */
    @Transactional
    public int CountByAllMeetingCancelAndTimeout() {
        return meetingDao.countByStatus(0) + meetingDao.countByStatus(-1);
    }

    @Transactional
    public Iterable<Meeting> findAllByStartTimeBetweenAndStatusGreaterThan(Timestamp beginTime, Timestamp overTime, int status) {
        return meetingDao.findAllByStartTimeBetweenAndStatusGreaterThan(beginTime, overTime, status);
    }

    @Transactional
    public Iterable<Meeting> findAllByStartTimeBetween(Timestamp beginTime, Timestamp overTime) {
        return meetingDao.findAllByStartTimeBetween(beginTime, overTime);
    }

    @Transactional
    public Iterable<Meeting> findAllByStartTimeAfterAndEndTimeBefore(Timestamp newTime, Timestamp nowTime1) {
        return meetingDao.findAllByStartTimeAfterAndEndTimeBefore(newTime, nowTime1);
    }

    /**
     * 查询过期未签到预约
     */
    @Transactional
    public Iterable<Meeting> findAllByEndTimeBefore(Timestamp nowTime) {
        return meetingDao.findAllByEndTimeBeforeAndStatus(nowTime, 1);
    }

    /**
     * 查询七天内所有用户会议所有教室预约情况-重新封装模式
     *
     * @return 预约情况
     */
    @Transactional
    public Iterable<Meeting> querySevenDayMeetOfUser(String username) {
        Timestamp nowTime = new Timestamp(new Date().getTime());
        Timestamp newTime = new Timestamp(new Date().getTime());
        newTime.setDate(nowTime.getDate() + 7);
        return meetingDao.findAllByStuNameAndEndTimeBetweenAndStatusGreaterThanOrderByStartTime(username, nowTime, newTime, 0);
    }

    /**
     * 查询已预约座位统计和违规统计
     *
     * @return 预约情况
     */
    @Transactional
    public Iterable<Meeting> queryReservationStats(Timestamp beginTime, Timestamp overTime, Collection<Integer> stats) {
        return meetingDao.findAllByStartTimeBetweenAndStatusIn(beginTime, overTime, stats);
    }

    /**
     * 依照状态查询活动-重新封装模式
     *
     * @return 预约情况
     */
    @Transactional
    public Iterable<Meeting> findAllByStatus(int status) {
        return meetingDao.findAllByStatus(status);
    }


    /**
     * 依照会议时间是否冲突-重新封装模式
     *
     * @return 是否冲突
     */
    @Transactional
    public String checkTimeConflict(Meeting meeting) {
        if (meetingDao.countByRoomIdAndStartTimeBeforeAndEndTimeAfterAndStatusGreaterThanAndSeatRowAndSeatCol(meeting.getRoomId(), meeting.getEndTime(), meeting.getStartTime(), 0, meeting.getSeatRow(), meeting.getSeatCol()) > 0) {
            return "时间或座位与已有预约冲突，请重新选择";
        }
        if (meetingDao.countByStartTimeBeforeAndEndTimeAfterAndStatusGreaterThanAndStuName(meeting.getEndTime(), meeting.getStartTime(), 0, meeting.getEmpName()) > 0) {
            return "已预约该时间段的其它座位，请不要重复占座";
        }
        return "";
    }

    /**
     * 查询指定用户可用活动中接近开始的那一个
     *
     * @param newTime  要查询的预约时间
     * @param endTime  最多可接受时间，为当前时间加六个小时
     * @param status   活动状态
     * @param username 用户ID
     * @return 具体活动
     */
    @Transactional
    public Meeting meetingTimeVerification(Timestamp newTime, Timestamp endTime, int status, String username) {
        return meetingDao.findByStartTimeAfterAndEndTimeBeforeAndStatusGreaterThanAndStuName(newTime, endTime, status, username);
    }

    /**
     * 根据时间统计占座数量
     */
    @Transactional
    public Map<Integer, Integer> countOccupySeats(Timestamp startTime, Timestamp endTime) {
        return meetingDao.countSeats(startTime.toString(), endTime.toString()).stream().collect(
                Collectors.toMap(map -> map.get("roomId"), map -> map.get("remainSeats"), (l, r) -> l));
    }
}
