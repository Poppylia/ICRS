package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MeetingDao extends CrudRepository<Meeting,Long> {
    List<Meeting> findAllByRoomId(long id);

    List<Meeting> findAllByStuName(String stuName);

    List<Meeting> findAllByStuNameAndStatus(String stuName,int status);

    List<Meeting> findAllByStuNameAndCanceledTimeNotNull(String stuName);

    int countByStatus(int status);

    List<Meeting> findAllByStartTimeBetweenAndStatusGreaterThan(Timestamp beginTime,Timestamp overTime,int status);

    List<Meeting> findAllByStartTimeBetween(Timestamp beginTime,Timestamp overTime);

    List<Meeting> findAllByStartTimeAfterAndEndTimeBefore(Timestamp newTime,Timestamp newTime1);

    List<Meeting> findAllByEndTimeBeforeAndStatus(Timestamp newTime, int status);

    List<Meeting> findAllByStuNameAndStartTimeBetweenAndStatus(String stuName,Timestamp beginTime,Timestamp overTime,int status);

    List<Meeting> findAllByStartTimeBetweenAndStatusIn(Timestamp beginTime,Timestamp overTime,Collection<Integer> stats);

    List<Meeting> findAllByStatus(int status);

    Meeting findByStartTimeAfterAndEndTimeBeforeAndStatusGreaterThanAndStuName(Timestamp openTime,Timestamp endTime,int status,String empName);

    List<Meeting> findAllByRoomIdAndStartTimeBeforeAndEndTimeAfterAndStatusGreaterThan(int roomId, Timestamp endTime, Timestamp startTime, int status);

    int countByRoomIdAndStartTimeBeforeAndEndTimeAfterAndStatusGreaterThanAndSeatRowAndSeatCol(int roomId, Timestamp endTime, Timestamp startTime, int status, int seatRow, int seatCol);

    @Query(nativeQuery=true, value = "select m.room_id as roomId, count(m.meeting_id) as remainSeats from meeting m where m.end_time > :startTime and m.start_time < :endTime and  m.status > 0 group by m.room_id")
    List<Map<String, Long>> countSeats(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
