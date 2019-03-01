package com.mayikt.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: liuwq
 * @date: 2019年  上午12:44:19
 * @version: V1.0
 * @Copyright:该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
 *            私自分享视频和源码属于违法行为。
 */
@Controller
public class PayController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
}
