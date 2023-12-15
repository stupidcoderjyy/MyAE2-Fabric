package com.stupidcoderx.modding.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标记那些只有在数据生成环境下才起作用的元素，防止混淆
 * @see Mod#isEnvDataGen
 */
@Retention(RetentionPolicy.SOURCE)
public @interface DataGenOnly {
}
