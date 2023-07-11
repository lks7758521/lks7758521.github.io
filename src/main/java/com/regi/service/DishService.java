package com.regi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regi.dto.DishDto;
import com.regi.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
