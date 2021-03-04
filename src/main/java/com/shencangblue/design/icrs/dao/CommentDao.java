package com.shencangblue.design.icrs.dao;

import com.shencangblue.design.icrs.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByTopicIdAndCommentIdIsNull(Integer bbsId, Pageable pageable);

    List<Comment> findAllByTopicIdAndCommentIdIn(Integer topicId, List<Integer> commentIds);

}
