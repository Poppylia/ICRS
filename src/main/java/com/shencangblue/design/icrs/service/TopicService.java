package com.shencangblue.design.icrs.service;

import com.shencangblue.design.icrs.dao.TopicDao;
import com.shencangblue.design.icrs.model.Topic;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TopicService {
    @Autowired
    private TopicDao dao;

    public Topic findById(Integer id) {
        return dao.findById(id).get();
    }

    public Page<Topic> findList(String author, String title, Integer page, Integer size) {
        Sort timeDesc = Sort.by(Sort.Direction.DESC, "lastReplyTime");
        PageRequest request = PageRequest.of(page, size, timeDesc);
        if (Strings.isNotBlank(author) || Strings.isNotBlank(title)) {
            return dao.findAllByAuthorLikeAndTitleLike(author, title, request);
        }
        return dao.findAll(request);
    }

    public void save(Topic topic) {
        dao.save(topic);
    }

    public void delete(Integer id) {
        dao.deleteById(id);
    }
}
