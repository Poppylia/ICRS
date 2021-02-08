package com.shencangblue.design.icrs.controller;

import com.shencangblue.design.icrs.model.Comment;
import com.shencangblue.design.icrs.model.Topic;
import com.shencangblue.design.icrs.result.Result;
import com.shencangblue.design.icrs.result.ResultFactory;
import com.shencangblue.design.icrs.service.CommentService;
import com.shencangblue.design.icrs.service.TopicService;
import com.shencangblue.design.icrs.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;

    @GetMapping("/comment/{size}/{page}")
    public Result list(Integer topicId, @PathVariable Integer page, @PathVariable Integer size) {
        return ResultFactory.buildSuccessResult(commentService.findList(topicId, page, size));
    }

    @PostMapping("/comment")
    public Result comment(@RequestBody @Valid Comment comment) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        String author = userService.findByUsername(username).getName();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        comment.setUsername(username);
        comment.setAuthor(author);
        comment.setTime(time);
        commentService.save(comment);

        // 更新topic表的最后发表人
        Topic topic = topicService.findById(comment.getTopicId());
        topic.setLastReply(author);
        topic.setLastReplyTime(time);
        topic.setReply(topic.getReply() + 1);
        topicService.save(topic);

        return ResultFactory.buildSuccessResult("评论成功");
    }

    @DeleteMapping("/comment")
    public Result delete(@RequestParam List<Integer> ids) {
        for (Integer id : ids) {
            commentService.delete(id);
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
