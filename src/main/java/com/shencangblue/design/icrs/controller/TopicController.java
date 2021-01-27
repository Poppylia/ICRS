package com.shencangblue.design.icrs.controller;

import com.shencangblue.design.icrs.model.Bbs;
import com.shencangblue.design.icrs.model.Topic;
import com.shencangblue.design.icrs.result.Result;
import com.shencangblue.design.icrs.result.ResultFactory;
import com.shencangblue.design.icrs.service.BbsService;
import com.shencangblue.design.icrs.service.TopicService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private BbsService bbsService;

    @GetMapping("/topic")
    public Result topicList(Integer bbsId, Integer page, Integer size){
        return ResultFactory.buildSuccessResult(topicService.findList(bbsId, page, size));
    }

    @RequestMapping("/topic/add")
    public Result addTopic(@RequestBody @Valid Topic topic){
        String author = SecurityUtils.getSubject().getPrincipal().toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        topic.setAuthor(author);
        topic.setTime(time);
        topicService.save(topic);

        // 更新bbs表的最后发表人
        Bbs bbs = bbsService.findById(topic.getBbsId());
        bbs.setLastReply(author);
        bbs.setLastReplyTime(time);
        bbsService.save(bbs);

        return ResultFactory.buildSuccessResult("添加成功");
    }

    @RequestMapping("/topic/update")
    public Result updateTopicContent(@RequestBody Topic topic){
        String content = topic.getContent();
        topic = topicService.findById(topic.getId());
        topic.setContent(content);
        topicService.save(topic);
        return ResultFactory.buildSuccessResult("修改标题成功");
    }


    @DeleteMapping("/topic")
    public Result deleteTopic(@RequestParam List<Integer> ids){
        for (Integer id : ids) {
            topicService.delete(id);
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }


    @GetMapping("/bbs/detail")
    public Result detail(@RequestParam Integer bbsId){
        return ResultFactory.buildSuccessResult(topicService.findByBbsId(bbsId));
    }
}
