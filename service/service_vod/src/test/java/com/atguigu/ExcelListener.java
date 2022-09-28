package com.atguigu;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuyusong
 * @create 2022-09-09 13:44
 */
public class ExcelListener extends AnalysisEventListener<Stu> {
    List<Stu> list = new ArrayList<Stu>();
    @Override
    public void invoke(Stu stu, AnalysisContext analysisContext) {
        System.out.println(stu);
        list.add(stu);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println(headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
