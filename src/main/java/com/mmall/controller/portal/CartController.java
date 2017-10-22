package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by yangzhou on 2017-08-19.
 */

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.list(user.getId());
    }


    // 加入count数目的product (with productId)
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.add(user.getId(), productId, count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession httpSession, String productIds) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        return iCartService.deleteProduct(user.getId(), productIds);
    }


    // select all
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, null);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, productId);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, productId);
    }


    // 获取购物城中产品数量
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}

