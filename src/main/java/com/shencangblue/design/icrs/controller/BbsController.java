package com.shencangblue.design.icrs.controller;

import com.shencangblue.design.icrs.model.Bbs;
import com.shencangblue.design.icrs.model.Topic;
import com.shencangblue.design.icrs.result.Result;
import com.shencangblue.design.icrs.result.ResultFactory;
import com.shencangblue.design.icrs.service.BbsService;
import com.shencangblue.design.icrs.service.TopicService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api")
public class BbsController {
    @Autowired
    private BbsService bbsService;
    @Autowired
    private TopicService topicService;


    @RequestMapping("/bbs/list")
    public Result bbsList(String author, String title, Integer page, Integer size) {
        return ResultFactory.buildSuccessResult(bbsService.findList(author, title, page, size));
    }

    // 发表新帖
    @RequestMapping("/bbs/add")
    public Result newTopic(@RequestBody @Valid Bbs bbs) {
        String content = bbs.getContent();
        String author = SecurityUtils.getSubject().getPrincipal().toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bbs.setAuthor(author);
        bbs.setTime(time);
        bbsService.save(bbs);

        Topic topic = new Topic();
        topic.setAuthor(author);
        topic.setBbsId(bbs.getId());
        topic.setContent(content);
        topic.setTime(time);
        topicService.save(topic);

        return ResultFactory.buildSuccessResult("发表新帖成功");

    }

    // 修改标题
    @RequestMapping("updateTitle")
    public Result updateBbsTitle(@RequestBody Bbs bbs) {
        String title = bbs.getTitle();
        bbs = bbsService.findById(bbs.getId());
        bbs.setTitle(title);
        bbsService.save(bbs);
        return ResultFactory.buildSuccessResult("修改标题成功");
    }


    @RequestMapping("/bbs/delete")
    public Result deleteBbs(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            bbsService.delete(id);
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }
}