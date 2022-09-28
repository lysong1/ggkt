package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuyusong
 * @create 2022-09-09 9:31
 */
public interface SubjectService extends IService<Subject> {
    List<Subject> selectList(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
