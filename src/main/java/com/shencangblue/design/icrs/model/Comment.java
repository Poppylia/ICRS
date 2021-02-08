package com.shencangblue.design.icrs.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer topicId;  //主题id
    private Integer commentId;  //回复的评论id
    private String content;  //内容
    private String username;   // 用户名
    private String author;   // 作者
    private String time;   //发表时间
    @Transient
    private List<Comment> replyList; //回复列表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }
}
