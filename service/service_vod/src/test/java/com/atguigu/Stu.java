package com.atguigu;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author liuyusong
 * @create 2022-09-09 10:47
 */
@Data
public class Stu {

    @ExcelProperty(value = "学生编号",index = 0)
    private int sno;

    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;

}
