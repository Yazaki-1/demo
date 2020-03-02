package com.example.demo.service.impl;

import com.example.demo.model.QSystemCoupon;
import com.example.demo.model.SystemCoupon;
import com.example.demo.model.bean.SysCouponDTO;
import com.example.demo.repository.SysCouponRepository;
import com.example.demo.service.SysCouponService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SysCouponServiceImpl implements SysCouponService {

    @Resource
    private SysCouponRepository sysCouponRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    /**
     * 生成初始系统优惠券
     * @param para
     * @return
     */
    public Object creatCoupon(@Valid @RequestBody SysCouponDTO para) {

        SystemCoupon systemCoupon = new SystemCoupon();
        BeanUtils.copyProperties(para , systemCoupon);
        String uuid = UUID.randomUUID().toString();
        systemCoupon.setCouponId(uuid);

        systemCoupon.setCreationTime(LocalDateTime.now());
        return sysCouponRepository.save(systemCoupon);
    }

    /**
     * 所有优惠券
     * @return
     */
    public List<SystemCoupon> findAll(){
        QSystemCoupon systemCoupon = QSystemCoupon.systemCoupon;
        return jpaQueryFactory
                .selectFrom(systemCoupon)
                .orderBy(systemCoupon.creationTime.asc())
                .fetch();
    }

    /**
     * 根据状态查询
     * @param status
     * @return
     */
    public List<SystemCoupon> findByStatus(String status){
        QSystemCoupon systemCoupon = QSystemCoupon.systemCoupon;
        return jpaQueryFactory
                .selectFrom(systemCoupon)
                .where(systemCoupon.status.eq(status))
                .orderBy(systemCoupon.creationTime.asc())
                .fetch();
    }
}
