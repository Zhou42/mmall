package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhou on 2017-09-16.
 */

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        // 通过设置 useGeneratedKeys="true" keyProperty="id" 在mapping中，使新生成的id 立刻被赋值在shipping object上
        int rowCount = shippingMapper.insert(shipping);

        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse del(Integer userId, Integer shippingId) {
        // 注意这里删除要避免横向越权
        int resultCount = shippingMapper.deleteByShippingIdUserId(shippingId, userId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping) {
        // shipping info使用户自己传的，各异造假，所以使用session中取出来的userId
        shipping.setUserId(userId);
        // 需要避免横向越权，要select userId以及shipping的id，来insert
        int rowCount = shippingMapper.updateByShipping(shipping);

        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId, userId);

        if (shipping  == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功", shipping);
    }

    public ServerResponse<PageInfo> list(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 此时的query会被自动加上分页部分
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        // 将shipping list封装成pageInfo
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
