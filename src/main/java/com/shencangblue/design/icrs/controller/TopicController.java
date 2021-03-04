package com.shencangblue.design.icrs.controller;

import com.shencangblue.design.icrs.model.Topic;
import com.shencangblue.design.icrs.result.Result;
import com.shencangblue.design.icrs.result.ResultFactory;
import com.shencangblue.design.icrs.service.TopicService;
import com.shencangblue.design.icrs.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;

    @GetMapping("/topic/{size}/{page}")
    public Result list(String author, String title, @PathVariable Integer page, @PathVariable Integer size) {
        return ResultFactory.buildSuccessResult(topicService.findList(author, title, page-1, size));
    }

    // 发表新帖
    @PostMapping("/topic")
    public Result post(@RequestBody @Valid Topic topic) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        String author = userService.findByUsername(username).getName();
        if (topic.getId() == null) {
            topic.setView(0);
            topic.setReply(0);
            topic.setUsername(username);
            topic.setAuthor(author);
            topic.setTime(time);
        } else {
            String title = topic.getTitle();
            String content = topic.getContent();
            topic = topicService.findById(topic.getId());
            topic.setTitle(title);
            topic.setContent(content);
        }
        topic.setLastReplyTime(time);
        topicService.save(topic);
        return ResultFactory.buildSuccessResult("发帖成功");
    }

    @GetMapping("/topic/{id}")
    public Result get(@PathVariable Integer id) {
        Topic topic = topicService.findById(id);
        topic.setView(topic.getView() + 1);
        topicService.save(topic);
        return ResultFactory.buildSuccessResult(topic);
    }

    @DeleteMapping("/topic/{id}")
    public Result delete(@PathVariable Integer id) {
        topicService.delete(id);
        return ResultFactory.buildSuccessResult("删除成功");
    }
}