package com.regi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regi.entity.OrderDetail;
import com.regi.mapper.OrderDetailMapper;
import com.regi.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}
