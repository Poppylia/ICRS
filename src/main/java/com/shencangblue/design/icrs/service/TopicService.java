package com.shencangblue.design.icrs.service;

import com.shencangblue.design.icrs.dao.TopicDao;
import com.shencangblue.design.icrs.model.Topic;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TopicService {

    @Resource
    private TopicDao dao;

    public Page<Topic> findList(Integer bbsId, Integer page, Integer size) {
        Sort timeDesc = Sort.by(Sort.Direction.DESC, "time");
        PageRequest request = PageRequest.of(page, size, timeDesc);
        if(bbsId != null) {
            return dao.findAllByBbsId(bbsId, request);
        }
        return dao.findAll(request);
    }

    public Topic findById(Integer id) {
        return dao.getOne(id);
    }

    public List<Topic> findByBbsId(Integer bbsId) {
        return dao.findAllByBbsId(bbsId);
    }

    public void save(Topic topic) {
        dao.save(topic);
    }

    public void delete(Integer id){
        dao.deleteById(id);
    }
}
