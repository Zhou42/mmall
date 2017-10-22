package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yangzhou on 2017-09-30.
 */

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;


    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return orderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return orderService.cancel(user.getId(), orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return orderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        return orderService.getOrderList(user.getId(), pageNum, pageSize);
    }






    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        // 这个跟上面的一样？？ String path2 = session.getServletContext().getRealPath("upload");

        return orderService.pay(orderNo, user.getId(), path);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();

        Map requestParameters = request.getParameterMap();

        for (Iterator iterator = requestParameters.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParameters.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        logger.info("支付宝回调, sign:{}, trade_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 验证回调的正确性 1) 是不是支付宝发的 2) 是否是重复的

        params.remove("sign_type");

        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("恶意请求！！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调异常", e);
        }

        // todo: 需验证返回的订单信息，是否与商户的一致

        ServerResponse serverResponse = orderService.aliCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }



    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER); // .getAttribute 返回的是一个Object 需要重新cast
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc()); // 用户没有登录，没法加入其购物车
        }

        ServerResponse serverResponse = orderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(Boolean.TRUE);
        } else {
            return ServerResponse.createBySuccess(Boolean.FALSE);
        }
    }
}
