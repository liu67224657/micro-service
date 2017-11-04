package com.enjoyf.platform.contentservice.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * abstract base resource
 * Created by ericliu on 2017/8/19.
 */
public abstract class AbstractBaseResource {

    @Autowired
    private MessageSource messageSource;


    protected String getMessage(String key, Object[] param) {
        return messageSource.getMessage(key, param, Locale.CHINA);
    }

}
