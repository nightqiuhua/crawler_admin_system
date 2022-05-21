package com.yitong.crawlerservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitong.commonutils.R;
import com.yitong.crawlerservice.entity.Admininstrator;
import com.yitong.crawlerservice.entity.vo.AdmininstratorQuery;
import com.yitong.crawlerservice.service.AdmininstratorService;
import com.yitong.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-14
 */
@RestController
@RequestMapping("/crawlerservice/admininstrator")
@CrossOrigin
public class AdmininstratorController {

    //把service注入
    @Autowired
    private AdmininstratorService admininstratorService;

    @GetMapping("findAll")
    public R findAllAdminStrator(){
        List<Admininstrator> list = admininstratorService.list(null);
        return R.ok().data("items",list);
    }

    @DeleteMapping("{id}")
    public boolean removeById(@PathVariable String id){
        return admininstratorService.removeById(id);
    }

    //3 分页查询讲师的方法
    //current 当前页
    //limit 每页记录数
    @GetMapping("pageAdmininstrator/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit) {
        //创建page对象
        Page<Admininstrator> pageAdmininstrator = new Page<>(current,limit);


        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        admininstratorService.page(pageAdmininstrator,null);

        long total = pageAdmininstrator.getTotal();//总记录数
        List<Admininstrator> records = pageAdmininstrator.getRecords(); //数据list集合

//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("rows",records);
    }

    //根据管理员id进行查询
    @GetMapping("getAdmininstrator/{id}")
    public R getTeacher(@PathVariable String id) {
        Admininstrator admininstrator = admininstratorService.getById(id);
        return R.ok().data("admininstrator",admininstrator);
    }


    //4 条件查询带分页的方法
    @PostMapping("pageAdmininstratorCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false) AdmininstratorQuery admininstratorQuery) {
        //创建page对象
        Page<Admininstrator> pageTeacher = new Page<>(current,limit);

        //构建条件
        QueryWrapper<Admininstrator> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = admininstratorQuery.getName();
        //Integer level = teacherQuery.getLevel();
        String begin = admininstratorQuery.getBegin();
        String end = admininstratorQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name",name);
        }
        //if(!StringUtils.isEmpty(level)) {
        //    wrapper.eq("level",level);
        //}
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        admininstratorService.page(pageTeacher,wrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<Admininstrator> records = pageTeacher.getRecords(); //数据list集合
        return R.ok().data("total",total).data("rows",records);
    }



    //添加管理员接口的方法
    @PostMapping("addAdmininstrator")
    public R addTeacher(@RequestBody Admininstrator eduTeacher) {
        boolean save = admininstratorService.save(eduTeacher);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //管理员修改功能
    @PostMapping("updateAdmininstrator")
    public R updateTeacher(@RequestBody Admininstrator eduTeacher) {
        boolean flag = admininstratorService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

