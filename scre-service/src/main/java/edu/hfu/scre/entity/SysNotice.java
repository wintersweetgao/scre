package edu.hfu.scre.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * 公告
 */
@Entity
public class SysNotice {
    private Integer noticeId;// 公告ID
    private String noticeTitle;// 公告标题
    private String noticeContent;// 公告内容
    private String noticeName;//创建者
    private Integer noticeReadvolume;// 阅读量
    protected Date createTime;//创建公告时间

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略
    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    @Column(columnDefinition = "TEXT", nullable = false)
    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public Integer getNoticeReadvolume() {
        return noticeReadvolume;
    }

    public void setNoticeReadvolume(Integer noticeReadvolume) {
        this.noticeReadvolume = noticeReadvolume;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Notice [noticeId=" + noticeId + ", noticeTitle=" + noticeTitle + ", noticeContent=" + noticeContent
                + ", noticeName=" + noticeName + ", noticeReadvolume=" + noticeReadvolume
                + ", createTime=" + createTime + "]";
    }


}