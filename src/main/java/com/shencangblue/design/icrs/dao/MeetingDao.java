package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.ClassRoom;
import com.shencangblue.design.icrs.model.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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

    @Query("select m.roomId, count(m.meetingId) as remainSeats from Meeting m where m.startTime < ?2 and m.endTime > ?1 and m.status > 0 group by m.roomId")
    List<ClassRoom> countSeats(Timestamp startTime, Timestamp endTime);
}
