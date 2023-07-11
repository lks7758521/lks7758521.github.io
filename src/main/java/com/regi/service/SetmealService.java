package com.regi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regi.dto.SetmealDto;
import com.regi.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long>ids);

    public void updatestatus(Integer status, List<Long> ids);
}
