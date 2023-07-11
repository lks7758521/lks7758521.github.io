package com.regi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regi.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
