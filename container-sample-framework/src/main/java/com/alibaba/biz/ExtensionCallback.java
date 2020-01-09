package com.alibaba.biz;

import java.util.function.Function;

public interface ExtensionCallback<T extends BizExtension, R> extends Function<T, R> {

    R apply(T extension);
}
