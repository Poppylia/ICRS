package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BbsDao extends JpaRepository<Bbs, Integer> {

    Page<Bbs> findAllByAuthorLikeAndTitleLike(String author, String title, Pageable pageable);
}
