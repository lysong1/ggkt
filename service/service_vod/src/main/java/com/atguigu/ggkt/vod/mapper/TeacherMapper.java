package com.atguigu.ggkt.vod.mapper;


import com.atguigu.ggkt.model.vod.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-08-30
 */
@Repository
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

}
