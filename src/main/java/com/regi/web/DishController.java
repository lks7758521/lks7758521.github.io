package com.regi.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regi.common.R;
import com.regi.dto.DishDto;
import com.regi.entity.Category;
import com.regi.entity.Dish;
import com.regi.entity.DishFlavor;
import com.regi.service.CategoryService;
import com.regi.service.DishFlavorSerive;
import com.regi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.tomcat.util.threads.StopPooledThreadException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorSerive dishFlavorSerive;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        String key = "dish_"+dishDto.getCategoryId()+"1";
        stringRedisTemplate.delete(key);
        return R.success("新增菜品成功");
    }
    @GetMapping("/page")
    public R<Page> page (int page,int pageSize,String name){
         Page<Dish> pageInfo = new Page<>(page,pageSize);
          Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        String key = "dish_"+dishDto.getCategoryId()+"1";
        stringRedisTemplate.delete(key);
        return R.success("修改菜品成功");
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList =null;
        String key = "dish"+dish.getCategoryId()+"_"+dish.getStatus();
        System.out.println(stringRedisTemplate.opsForValue().get(key));
        dishDtoList =(List<DishDto>) JSON.parse(stringRedisTemplate.opsForValue().get(key));
        if(dishDtoList!=null){
            return R.success(dishDtoList);
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

         dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishid = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishid);
            List<DishFlavor> dishFlavorList = dishFlavorSerive.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(dishDtoList),60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }

    @PostMapping("/status/{status}")
    public R<Dish> updatestatus(@PathVariable Integer status, Long ids){
      log.info("状态id:{}",ids);
        Dish dish = new Dish();
        dish.setId(ids);
        dish.setStatus(status);
        dishService.updateById(dish);
        return R.success(dish);
    }

}
