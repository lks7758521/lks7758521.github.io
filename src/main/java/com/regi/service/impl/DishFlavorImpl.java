package com.regi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regi.entity.DishFlavor;
import com.regi.mapper.DishFlavorMapper;
import com.regi.service.DishFlavorSerive;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorSerive {
}
