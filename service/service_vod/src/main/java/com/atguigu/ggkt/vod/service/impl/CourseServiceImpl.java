package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.*;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-11
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private VideoService videoService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Override
    public Map<String, Object> findPage(Page<Course> coursePage, CourseQueryVo courseQueryVo) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Long subjectId = courseQueryVo.getSubjectId();
        Long subjectParentId = courseQueryVo.getSubjectParentId();
        Long teacherId = courseQueryVo.getTeacherId();
        String title = courseQueryVo.getTitle();

        if(!StringUtils.isEmpty(title)) {
            queryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id", subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id",teacherId);
        }

        Page<Course> page = baseMapper.selectPage(coursePage, queryWrapper);
        long current = page.getCurrent();
        long pages = page.getPages();
        long total = page.getTotal();
        long size = page.getSize();
        List<Course> records = page.getRecords();
        records.stream().forEach(item -> {
            this.getNameById(item);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount",total);
        map.put("totalPage", pages);
        map.put("records", records);

        return map;
    }


    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //view_count流量数量+1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        //根据课程ID查询
        //课程详情数据
       CourseVo courseVo = baseMapper.selectCourseVoById(courseId);
        //课程章节小节数据
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
        //课程描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        //课程所属讲师信息
        Teacher teacher = teacherService.getById(course.getTeacherId());
        //封装map集合，返回
        Map<String,Object> map = new HashMap<>();

        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", false);//是否购买
        return map;
    }

    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.stream().forEach(item -> {
            this.getNameById(item);
        });
        return list;
    }


    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程基本信息   操作Course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);

        //添加课程描述信息   操作Course_description
        CourseDescription courseDescription = new CourseDescription();
        BeanUtils.copyProperties(courseFormVo, courseDescription);
        courseDescription.setId(course.getId());

        courseDescriptionService.save(courseDescription);

        return course.getId();
    }


    //根据ID查询课程信息
    @Override
    public CourseFormVo getCourseInfoById(Long id) {

        CourseFormVo courseFormVo = new CourseFormVo();
        Course course = baseMapper.selectById(id);
        if (course == null){
            return null;
        }
        CourseDescription courseDescription = courseDescriptionService.getById(id);

        BeanUtils.copyProperties(course, courseFormVo);
        if (courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    @Override
    public void updateCourseId(CourseFormVo courseFormVo) {

        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.updateById(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(Long id) {
        CoursePublishVo coursePublishVo = baseMapper.selectCoursePublishVoById(id);
        return coursePublishVo;
    }

    @Override
    public void publishCourse(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);
        course.setPublishTime(new Date());
        baseMapper.updateById(course);

    }

    @Override
    public void removeCourseById(Long id) {
        videoService.removeVideoByCourseId(id);

        chapterService.removeChapterByCourseId(id);

        courseDescriptionService.removeById(id);

        baseMapper.deleteById(id);
    }

    private Course getNameById(Course course) {

        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null){
            course.getParam().put("teacherName", teacher.getName());
        }

        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if (subjectOne != null){
            course.getParam().put("subjectParentTitle", subjectOne.getTitle());
        }

        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if (subjectTwo != null){
            course.getParam().put("subjectTitle", subjectTwo.getTitle());
        }

        return course;

    }
}
