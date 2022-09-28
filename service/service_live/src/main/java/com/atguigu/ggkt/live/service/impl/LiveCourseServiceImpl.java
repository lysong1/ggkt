package com.atguigu.ggkt.live.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.client.course.CourseFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.live.mapper.LiveCourseMapper;
import com.atguigu.ggkt.live.mtcloud.CommonResult;
import com.atguigu.ggkt.live.mtcloud.MTCloud;
import com.atguigu.ggkt.live.service.*;
import com.atguigu.ggkt.model.live.*;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.utils.DateUtil;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseGoodsView;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-26
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {


    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @Autowired
    private LiveCourseDescriptionService  liveCourseDescriptionService;

    @Autowired
    private CourseFeignClient teacherFeignClient;

    @Autowired
    private LiveCourseConfigService liveCourseConfigService;

    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    @Resource
    private MTCloud mtCloudClient;


    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        Page<LiveCourse> pageModel = baseMapper.selectPage(pageParam, null);
        List<LiveCourse> liveCourses = pageModel.getRecords();
        for (LiveCourse liveCourse : liveCourses) {
            Teacher teacher = teacherFeignClient.getTeacherInfo(liveCourse.getTeacherId());
            liveCourse.getParam().put("teacherName",teacher.getName());
            liveCourse.getParam().put("teacherLevel", teacher.getLevel());
        }
        return pageModel;
    }

    //直播课程添加
    @Override
    public void saveLive(LiveCourseFormVo liveCourseFormVo) {
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo, liveCourse);

        Teacher teacher = teacherFeignClient.getTeacherInfo(liveCourse.getTeacherId());

        HashMap<Object, Object> options = new HashMap<>();
        options.put("scenes", 2);
        options.put("password", liveCourseFormVo.getPassword());

        try {
            String res = mtCloudClient.courseAdd(liveCourse.getCourseName(),
                    teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);

            System.out.println("res:" + res);

            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS){
                JSONObject object = commonResult.getData();
                liveCourse.setId(object.getLong("course_id"));

                baseMapper.insert(liveCourse);

                //保存课程详情信息
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                liveCourseDescriptionService.save(liveCourseDescription);

                //保存课程账号信息
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(object.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setAdminKey(object.getString("admin_key"));
                liveCourseAccount.setUserKey(object.getString("user_key"));
                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
                liveCourseAccountService.save(liveCourseAccount);



            }else {
                String getmsg = commonResult.getmsg();
                throw new GgktException(20001,getmsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void removeLive(Long id) {
        //根据id查询直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        if (liveCourse != null){
            //获取直播courseid
            Long courseId = liveCourse.getCourseId();

            try {
                //获取直播courseid
                mtCloudClient.courseDelete(courseId.toString());
                //删除表数据
                baseMapper.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GgktException(20001,"删除直播课程失败");
            }
        }
    }

    @Override
    public LiveCourseFormVo getLiveCourseFormVo(Long id) {
        LiveCourse liveCourse = baseMapper.selectById(id);

       LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(id);

        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
        BeanUtils.copyProperties(liveCourse, liveCourseFormVo);
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }

    @Override
    public void updateLiveById(LiveCourseFormVo liveCourseFormVo) {
        //根据id获取直播课程基本信息
        LiveCourse liveCourse = baseMapper.selectById(liveCourseFormVo.getId());
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);
        //讲师
        Teacher teacher =
                teacherFeignClient.getTeacherInfo(liveCourseFormVo.getTeacherId());

        //             *   course_id 课程ID
//     *   account 发起直播课程的主播账号
//     *   course_name 课程名称
//     *   start_time 课程开始时间,格式:2015-01-01 12:00:00
//                *   end_time 课程结束时间,格式:2015-01-01 13:00:00
//                *   nickname 	主播的昵称
//                *   accountIntro 	主播的简介
//                *  options 		可选参数
        HashMap<Object, Object> options = new HashMap<>();

        try {
            String res = mtCloudClient.courseUpdate(liveCourse.getCourseId().toString(),
                    teacher.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);

            //返回结果转换，判断是否成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                //更新直播课程基本信息
                liveCourse.setCourseId(object.getLong("course_id"));
                baseMapper.updateById(liveCourse);

                //直播课程描述信息更新
                LiveCourseDescription liveCourseDescription =
                        liveCourseDescriptionService.getLiveCourseById(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescriptionService.updateById(liveCourseDescription);
            } else {
                throw new GgktException(20001,"修改直播课程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public LiveCourseConfigVo getCoucrseConfig(Long id) {
        //根据课程ID查询配置信息
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getCourseConfigCourseId(id);

        //封装LiveCourseConfigVo
        LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();

        if (liveCourseConfig != null){
            //查询直播课程列表
            List<LiveCourseGoods> liveCourseGoods =liveCourseGoodsService.getGoodsListCourseId(id);

            //封装到LiveCourseConfigVo里面
            BeanUtils.copyProperties(liveCourseConfig, liveCourseConfigVo);
            //封装商品列表
            liveCourseConfigVo.setLiveCourseGoodsList(liveCourseGoods);


        }

        return liveCourseConfigVo;
    }

    //修改配置
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {

        //1 修改直播配置表
        LiveCourseConfig liveCourseConfig = new LiveCourseConfig();
        BeanUtils.copyProperties(liveCourseConfigVo,liveCourseConfig);
        if (liveCourseConfigVo.getId() == null){
            liveCourseConfigService.save(liveCourseConfig);
        }else {
            liveCourseConfigService.updateById(liveCourseConfig);
        }
        //2 修改直播商品表
        //根据ID删除直播商品列表
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,liveCourseConfigVo.getLiveCourseId());
        liveCourseGoodsService.remove(wrapper);
        //添加商品
        if (!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())){
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());
        }

        //3 修改在直播平台
        this.updateLifeConfig(liveCourseConfigVo);

    }

    //获取最近的直播
    @Override
    public List<LiveCourseVo> findLatelyList() {
       List<LiveCourseVo> liveCourseVoList = baseMapper.getLatelyList();
        for (LiveCourseVo liveCourseVo : liveCourseVoList) {
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));
            //封装讲师
            Long teacherId = liveCourseVo.getTeacherId();
            Teacher teacher = teacherFeignClient.getTeacherInfo(teacherId);
            liveCourseVo.setTeacher(teacher);

            //封装直播状态
            liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));

        }

        return liveCourseVoList;

    }

    @Override
    public JSONObject getAccessToken(Long id, Long userId) {
        //根据课程ID获取直播信息
        LiveCourse liveCourse = baseMapper.selectById(id);

        //根据用户ID获取用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);

        HashMap<Object, Object> options = new HashMap<>();
        try {
            String res = mtCloudClient.courseAccess(liveCourse.getCourseId().toString(),
                    userId.toString(),
                    userInfo.getNickName(),
                    MTCloud.ROLE_USER,
                    3600,
                    options);
            CommonResult<JSONObject> commonResult = JSONObject.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                System.out.println("access::"+object.getString("access_token"));
                return object;
            } else {
                throw new GgktException(20001,"获取失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getInfoById(Long id) {
        LiveCourse liveCourse = this.getById(id);
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        Teacher teacher = teacherFeignClient.getTeacherInfo(liveCourse.getTeacherId());
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != liveCourseDescription) {
            map.put("description", liveCourseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }


    /**
     * 直播状态 0：未开始 1：直播中 2：直播结束
     * @param liveCourse
     * @return
     */
    private int getLiveStatus(LiveCourse liveCourse) {
        // 直播状态 0：未开始 1：直播中 2：直播结束
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    //    修改在直播平台
    private void updateLifeConfig(LiveCourseConfigVo liveCourseConfigVo) {
        LiveCourse liveCourse = baseMapper.selectById(liveCourseConfigVo.getLiveCourseId());

        //参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        //界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        //观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        //观看人数开关
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());
        number.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        //商城列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }

        try {
            String res = mtCloudClient.courseUpdateLifeConfig(liveCourse.getCourseId().toString(),
                    options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new GgktException(20001,"修改配置信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
