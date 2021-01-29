package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicDao extends JpaRepository<Topic, Integer> {

    Page<Topic> findAllByBbsId(Integer bbsId, Pageable pageable);

    List<Topic> findAllByBbsId(Integer bbsId);
}
