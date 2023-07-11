package com.regi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regi.entity.ShoppingCart;
import com.regi.mapper.ShoppingCartMapper;
import com.regi.service.SetmealService;
import com.regi.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
