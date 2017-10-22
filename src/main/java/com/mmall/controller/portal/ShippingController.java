package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by yangzhou on 2017-09-16.
 */

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;


    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {

        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping) {

        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer shippingId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpSession session
                                         ) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
