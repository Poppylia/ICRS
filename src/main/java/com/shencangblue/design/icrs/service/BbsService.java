package com.shencangblue.design.icrs.service;

import com.shencangblue.design.icrs.dao.BbsDao;
import com.shencangblue.design.icrs.model.Bbs;
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
public class BbsService {
    @Autowired
    private BbsDao dao;

    public Bbs findById(Integer id) {
        return dao.getOne(id);
    }

    public Page<Bbs> findList(String author, String title, Integer page, Integer size) {
        Sort timeDesc = Sort.by(Sort.Direction.DESC, "lastReplyTime");
        PageRequest request = PageRequest.of(page, size, timeDesc);
        if (Strings.isNotBlank(author) || Strings.isNotBlank(title)) {
            return dao.findAllByAuthorLikeAndTitleLike(author, title, request);
        }
        return dao.findAll(request);
    }

    public void save(Bbs bbs) {
        dao.save(bbs);
    }

    public void delete(Integer id) {
        dao.deleteById(id);
    }

    public Bbs findOneBbs(Integer id) throws Exception {
        return dao.getOne(id);
    }

}
