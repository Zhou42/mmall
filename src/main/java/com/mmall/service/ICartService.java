package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by yangzhou on 2017-08-19.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}