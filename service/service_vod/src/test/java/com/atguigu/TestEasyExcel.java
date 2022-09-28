package com.atguigu;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyusong
 * @create 2022-09-09 11:03
 */
public class TestEasyExcel {
//    public static void main(String[] args) {
//        String file = "F:\\11.xlsx";
//
//        EasyExcel.write(file,Stu.class)
//                .sheet("写入")
//                .doWrite(data());
//    }
//
//    private static List<Stu> data() {
//        List<Stu> list = new ArrayList<Stu>();
//        for (int i = 0; i < 10; i++) {
//            Stu data = new Stu();
//            data.setSno(i);
//            data.setSname("张三"+i);
//            list.add(data);
//        }
//        return list;
//    }


    public static void main(String[] args) {

        String file = "F:\\java尚硅谷\\尚硅谷硅谷课堂项目\\资料\\subject.xlsx";
        EasyExcel.read(file, Stu.class, new ExcelListener()).sheet().doRead();
    }
}
