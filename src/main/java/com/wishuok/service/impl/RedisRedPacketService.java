package com.wishuok.service.impl;

import com.wishuok.pojo.UserRedPacket;
import com.wishuok.service.IRedPacketService;
import com.wishuok.service.IRedisRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisRedPacketService implements IRedisRedPacketService {
    //  每次取出 800 条 ，避免一次取出消耗太多内存
    private static final int TIME_SIZE = 800;
    // redis中每个用户抢红包结果的list key
    private static final String REDIS_PREFIX = "red_packet_list_";
    @Autowired
    private RedisTemplate stringRedisTemplate = null;

    @Autowired
    private DataSource dataSource = null;

    @Override
    @Async // 开启新线程运行
    public void saveUserRedPacketByRedis(int redPacketId, double unitAmout) {
        System.err.println("开始保存数据 " + "thread_name = " + Thread.currentThread().getName());
        String redisKey =  REDIS_PREFIX + redPacketId;
        Long startTime = System.currentTimeMillis();
        BoundListOperations ops = stringRedisTemplate.boundListOps(redisKey);
        Long size = ops.size();
        Long times = size % TIME_SIZE == 0 ? size / TIME_SIZE : size / TIME_SIZE + 1;
        int count = 0;
        List<UserRedPacket> userRedPacketList = new ArrayList<UserRedPacket>(TIME_SIZE);
        for (int i = 0; i < times; i++) {
            // 每次获取 TIME_SIZE 个信息
            List useridList = null;
            if (i == 0) {
                useridList = ops.range(0, TIME_SIZE);
            } else {
                useridList = ops.range(i * TIME_SIZE + 1, (i + 1) * TIME_SIZE);
            }
            userRedPacketList.clear();
            for (int j=0;j<useridList.size();j++){
                String args = useridList.get(j).toString();  // 存储的值为 userId + "-" + System.currentTimeMillis();
                String[] arr = args.split("-");
                String userIdStr = arr[0];
                String timeStr = arr[1];
                Integer userId = Integer.parseInt(userIdStr);
                Long time = Long.parseLong(timeStr);

                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(new BigDecimal(unitAmout, MathContext.DECIMAL32));
                userRedPacket.setGrabTime(new Timestamp(time));
                userRedPacket.setNote("抢红包" + redPacketId);
                userRedPacketList.add(userRedPacket);
            }
            // 插入数据库
            count += executeBatch(userRedPacketList);
        }
        // 删除 redis 列表
        stringRedisTemplate.delete(redisKey) ;
        Long end = System.currentTimeMillis();
        System.err.println("保存数据结束，耗时" + (end-startTime) +"毫秒，共" + count +"条记录被保存。");
    }

    /** 使用 JDBC 批量处理redis数据
     * @param userRedPacketList
     * @return 插入数量
     */
    private int executeBatch(List<UserRedPacket> userRedPacketList) {
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection conn = null;
        Statement stmt = null;
        int[] count = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (UserRedPacket packet: userRedPacketList) {
                String sql1 = "update t_red_packet set stock=stock-1 where id=" + packet.getRedPacketId();
                String sql2 = "insert into t_user_red_packet(red_packet_id,user_id,amount,grab_time,note)" +
                        " values(" +packet.getRedPacketId()+","+packet.getUserId()+","+packet.getAmount()+","
                        +"'"+dt.format(packet.getGrabTime()) +"','" +packet.getNote()+ "')";
                stmt.addBatch(sql1);
                stmt.addBatch(sql2);
            }
            count = stmt.executeBatch();    // 执行批量脚本
            conn.commit();                  // 提交事物
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("抢红包批量执行程序错误:"+ e.getMessage());
        }finally {
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 返回插入的记录数
        return count.length / 2;
    }
}
