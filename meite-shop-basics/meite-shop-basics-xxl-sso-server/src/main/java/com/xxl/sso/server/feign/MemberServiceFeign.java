package com.xxl.sso.server.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.mayikt.member.service.MemberService;

/**
 * @description:
 * @author: liuwq
 * @date: 2019年  下午11:36:17
 * @version: V1.0
 * @Copyright:该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
 *            私自分享视频和源码属于违法行为。
 */
@FeignClient(name = "app-mayikt-member")
public interface MemberServiceFeign extends MemberService {

}
