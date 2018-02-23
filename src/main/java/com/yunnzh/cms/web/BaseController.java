package com.yunnzh.cms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>All rights Reserved, Designed By HQYG.</p>
 *
 * @Copyright: Copyright(C) 2016.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/1214:36
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpServletRequest request;
}
