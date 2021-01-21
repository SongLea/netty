/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Special {@link EventExecutorGroup} which allows registering {@link Channel}s that get
 * processed for later selection during the event loop.
 *
 */
/*
    总结一下整个EventLoopGroup的初始化过程
    1、EventLoopGroup(其实是MultithreadEventExecutorGroup)内部维护一个类型为EventExecutor的children数组，其大小是nThreads,
       这样就构成了一个线程池。
    2、我们在实例化NioEventLoopGroup时，如果指定线程池大小，则nThreads就是指定的值，反之是CPU核数*2
    3、在MultithreadEventExecutorGroup中调用newChild()方法来初始化children数组
    4、newChild()方法是在NioEventLoopGroup中实现的，它返回一个NioEventLoop实例
    5、初始化NioEventLoop对象并给属性赋值，具体赋值的属性如下：
        provider:就是在NioEventLoopGroup构造器中，调用SelectorProvider.provider()方法获取的SelectorProvider对象
        selector:就是在NioEventLoop构造器中，调用provider.openSelector()方法获取的Selector对象
 */
public interface EventLoopGroup extends EventExecutorGroup {
    /**
     * Return the next {@link EventLoop} to use
     */
    @Override
    EventLoop next();

    /**
     * Register a {@link Channel} with this {@link EventLoop}. The returned {@link ChannelFuture}
     * will get notified once the registration was complete.
     */
    ChannelFuture register(Channel channel);

    /**
     * Register a {@link Channel} with this {@link EventLoop} using a {@link ChannelFuture}. The passed
     * {@link ChannelFuture} will get notified once the registration was complete and also will get returned.
     */
    ChannelFuture register(ChannelPromise promise);

    /**
     * Register a {@link Channel} with this {@link EventLoop}. The passed {@link ChannelFuture}
     * will get notified once the registration was complete and also will get returned.
     *
     * @deprecated Use {@link #register(ChannelPromise)} instead.
     */
    @Deprecated
    ChannelFuture register(Channel channel, ChannelPromise promise);
}
