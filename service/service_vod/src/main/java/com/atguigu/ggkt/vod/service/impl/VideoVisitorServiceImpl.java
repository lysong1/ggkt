package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.atguigu.ggkt.vod.mapper.VideoVisitorMapper;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-13
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {


    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        List<VideoVisitorCountVo> videoVisitorCountVoList = baseMapper.findCount(courseId, startDate, endDate);
        Map<String, Object> map = new HashMap<>();

        List<String> dateList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getJoinTime).
                collect(Collectors.toList());

        List<Integer> countList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());

        //放到map集合
        map.put("xData", dateList);
        map.put("yData", countList);

        return map;
    }
}
