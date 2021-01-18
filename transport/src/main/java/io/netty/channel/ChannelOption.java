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

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AbstractConstant;
import io.netty.util.ConstantPool;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * A {@link ChannelOption} allows to configure a {@link ChannelConfig} in a type-safe
 * way. Which {@link ChannelOption} is supported depends on the actual implementation
 * of {@link ChannelConfig} and may depend on the nature of the transport it belongs
 * to.
 *
 * @param <T>   the type of the value which is valid for the {@link ChannelOption}
 */
public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>> {

    private static final ConstantPool<ChannelOption<Object>> pool = new ConstantPool<ChannelOption<Object>>() {
        @Override
        protected ChannelOption<Object> newConstant(int id, String name) {
            return new ChannelOption<Object>(id, name);
        }
    };

    /**
     * Returns the {@link ChannelOption} of the specified name.
     */
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(String name) {
        return (ChannelOption<T>) pool.valueOf(name);
    }

    /**
     * Shortcut of {@link #valueOf(String) valueOf(firstNameComponent.getName() + "#" + secondNameComponent)}.
     */
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (ChannelOption<T>) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    /**
     * Returns {@code true} if a {@link ChannelOption} exists for the given {@code name}.
     */
    public static boolean exists(String name) {
        return pool.exists(name);
    }

    /**
     * Creates a new {@link ChannelOption} for the given {@code name} or fail with an
     * {@link IllegalArgumentException} if a {@link ChannelOption} for the given {@code name} exists.
     */
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> newInstance(String name) {
        return (ChannelOption<T>) pool.newInstance(name);
    }

    /*
        ByteBuf的分配器，默认值为ByteBufAllocator.DEFAULT，4.0版本为UnpooledByteBufAllocator，4.1版本为PooledByteBufAllocator
        分别对应的字符串值为'unpooled'与'pooled'
     */
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
    /*
        用于Channel分配接收Buffer的分配器，默认值为AdaptiveRecvByteBufAllocator.DEFAULT，是一个自适应的接收缓冲区分配器，能根据接收
        的数据自动调节大小。可选值为FixedRecvByteBufAllocator，固定大小的接收缓冲区分配器
     */
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
    /*
        消息大小估算器，默认值为DefaultMessageSizeEstimator.DEFAULT。估算ByteBuf、ByteBufHolder和FileRegion的大小，其中
        ByteBuf、ByteBufHolder为实际大小，FileRegion估算值为0。该值估算的字节数在计算水位时使用，FileRegion为0可知FileRegion不影响
        高低水位
     */
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");

    /*
        连接超时毫秒数，默认值为30_000ms,即30s
     */
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
    /**
     * @deprecated Use {@link MaxMessagesRecvByteBufAllocator}
     */
    @Deprecated
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
    /*
        一个Loop写操作执行的最大次数，默认值是16，也就是说，对于大数据量的写操作至多进行16次，如果16次仍没有全部写完数据，那么此时会提交一个新的
        写任务给EventLoop，任务将在下次调度继续执行。这样其他写请求才能被响应，不会因为单个大数据量写请求而耽误。
     */
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
    /**
     * @deprecated Use {@link #WRITE_BUFFER_WATER_MARK}
     */
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
    /**
     * @deprecated Use {@link #WRITE_BUFFER_WATER_MARK}
     */
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
    /*
        设置某个连接上可以暂存的最大最小Buffer，若该连接等待发送的数据量大于设置的值，则isWritable()会返回不可写，这样客户端可以不再发送，
        防止这个量不断地积压，最终可能让客户端挂掉
     */
    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK =
            valueOf("WRITE_BUFFER_WATER_MARK");

    /*
        一个连接的远端关闭时本地端是否关闭，默认值是false，值为true时连接自动关闭
     */
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
    /*
        自动读取，默认值为true，Netty只在必要的时候才设置关心相应的I/O事件，对于读操作，需要调用channel.read()设置关心的I/O事件为OP_READ，
        这样若有数据到达才能读取以供用户处理
     */
    public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");

    /**
     * If {@code true} then the {@link Channel} is closed automatically and immediately on write failure.
     * The default value is {@code true}.
     */
    public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");

    /*
        设置广播模式
     */
    public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
    /*
        连接保活，默认值为false。启用该功能时，TCP会主动探测空闲连接的有效性，可以将此功能视为TCP的心跳机制，需要注意的是，默认的心跳间隔是
        7200s，即2h，Netty默认关闭该功能。
     */
    public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
    /*
        TCP数据发送缓冲区大小。该缓冲区即TCP发送滑动窗口，Linux操作系统可使用命令cat/proc/sys/net/ipv4/tcp_smem查询其大小。
     */
    public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
    /*
        TCP数据接收缓冲区大小。该缓冲区即TCP接收滑动窗口，Linux操作系统可使用命令cat/proc/sys/net/ipv4/tcp_rmem查询其大小。一般情况下，
        该值可由用户在任意时刻设置，但当设置值超过64KB时，需要在连接到远端之前设置。
     */
    public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
    /*
        地址复用，默认值为false。有四种情况下可以使用：
        1.当有一个有相同本地地址和端口的Socket1处于TIME_WAIT状态时，你希望启动的程序的Socket2要占用该地址和端口，比如重启服务且保持先前端口。
        2.有多块网卡或用IP_Alias技术的机器在同一端口启动多个进程，但每个进程绑定的本地IP地址不能相同。
        3.单个进程绑定相同的端口到多个Socket，但每个Socket绑定的IP地址不同。
        4.完全相同的地址和端口的重复绑定，但这只用于UDP的多播，不用于TCP。
     */
    public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
    /*
        关闭Socket的延迟时间，默认值为-1，表示禁用该功能。-1表示socket.close()方法立即返回，但操作系统底层会将发送缓冲区的数据全部发送到对端，
        0表示socket.close()方法立即返回，操作系统放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误。非0整数表示调用socket.close()
        方法的线程被阻塞直接延迟时间到或缓冲区的数据发送完毕，若超时，则对端会收到复位错误。
     */
    public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
    /*
        服务端接收连接的队列长度，如果队列已满，客户端连接将被拒绝，默认值Window为200，其他为128
     */
    public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
    /*
        用于设置接收数据的等待超时时间，单位为ms，默认值为0表示无限等待。
     */
    public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");

    /*
        设置IP头部的Type-of-Service属性，用于描述IP包的优先级和QoS选项
     */
    public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
    /*
        对应IP参数IP_MULTICAST_IF，设置对应地址的网卡为多播模式
     */
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
    /*
        对应IP参数IP_MULTICAST_IF2，同上但支持IPv6
     */
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
    /*
        多播数据报的Time-to-Live，即存活跳数
     */
    public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
    /*
        对应IP参数IP_MULTICAST_LOOP，设置本地回环接口的多播功能。由于IPC_MULTICAST_LOOP返回true表示关闭，所以Netty加上后缀
        _DISABLED防止歧义
     */
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");

    /*
        默认值为true(Netty默认为true而操作系统默认为false)，该值设置Nagle算法的启用。
        Nagle算法：它用于自动连接许多的小缓冲器消息；这一过程（称为nagling）通过减少必须发送包的个数来增加网络软件系统的效率。
     */
    public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");

    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION =
            valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");

    /*
        单线程执行ChannelPipeline中的事件，默认值为true。该值控制执行ChannelPipeline中执行ChannelHandler的线程，如果为true，整个
        pipeline由一个线程执行，这样不需要进行线程切换以及线程同步，是Netty 4的推荐做法；如果为false，ChannelHandler中的处理过程会由
        Group中的不同线程执行
     */
    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP =
            valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");

    /**
     * Creates a new {@link ChannelOption} with the specified unique {@code name}.
     */
    private ChannelOption(int id, String name) {
        super(id, name);
    }

    @Deprecated
    protected ChannelOption(String name) {
        this(pool.nextId(), name);
    }

    /**
     * Validate the value which is set for the {@link ChannelOption}. Sub-classes
     * may override this for special checks.
     */
    public void validate(T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
    }
}
