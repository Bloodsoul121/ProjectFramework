package com.bloodsoul.projectframework.common;

import com.bloodsoul.projectframework.util.PlatformUtil;

public class Config {

    // 是否是debug模式
    public static final boolean DEBUG = true;
    // 兼容7.0的文件权限
    public static final String FILE_PROVIDER_AUTHORITIES = PlatformUtil.getPackageName() + ".fileprovider";

}
