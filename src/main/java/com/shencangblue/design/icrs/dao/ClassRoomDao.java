package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.ClassRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassRoomDao extends CrudRepository<ClassRoom,Integer> {

    List<ClassRoom> findAllByCapacityBetween(int capacity, int larger);

    List<ClassRoom> findAllByRoomNameLikeOrPositionLike(String key1,String key2);

    int countAllByStatus(int status);
    int countByPosition(String position);
    int findAllByPosition(String position);
    List<ClassRoom> findAllByStatus(int status);
    ClassRoom getByRoomName(String roomName);


}
