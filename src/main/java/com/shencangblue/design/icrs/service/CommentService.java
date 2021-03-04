package com.shencangblue.design.icrs.service;

import com.shencangblue.design.icrs.dao.CommentDao;
import com.shencangblue.design.icrs.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    @Resource
    private CommentDao dao;

    public Page<Comment> findList(Integer topicId, Integer page, Integer size) {
        Sort timeDesc = Sort.by(Sort.Direction.DESC, "time");
        PageRequest request = PageRequest.of(page, size, timeDesc);

        Page<Comment> commentPage = dao.findAllByTopicIdAndCommentIdIsNull(topicId, request);
        List<Integer> commentIds = commentPage.getContent().stream().map(Comment::getId).collect(Collectors.toList());
        if(!commentIds.isEmpty()) {
            Map<Integer, List<Comment>> map = dao.findAllByTopicIdAndCommentIdIn(topicId, commentIds).stream()
                    .collect(Collectors.groupingBy(Comment::getCommentId));
            commentPage.getContent().forEach(item -> item.setReplyList(map.get(item.getId())));
        }
        return commentPage;
    }

    public Comment findById(Integer id) {
        return dao.findById(id).get();
    }

    public void save(Comment comment) {
        dao.save(comment);
    }

    public void delete(Integer id) {
        dao.deleteById(id);
    }
}
