package com.regi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regi.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
