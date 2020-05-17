package com.changgou.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

/**
 * 实现canal数据监听
 */
@CanalEventListener
public class CanalDataEventListener {

    /**
     * @InsertListenPoint:增加监听 只监听增加
     * rowData.getAfterColumList(): 适用于增加,修改
     * rowData.getBeforeColumList(): 适用于删除,修改
     * @param entryType: 当前操作的类型 增加数据
     * @param rowData: 发生变更的一行数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        for (CanalEntry.Column colum: rowData.getAfterColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------增加的数据"+colum.getValue());
        }
    }

    /**
     * @UpdateListenPoint:增加监听 既能监听增加前,也能监听增加后
     * rowData.getAfterColumList(): 适用于增加,修改
     * rowData.getBeforeColumList(): 适用于删除,修改
     * @param entryType: 当前操作的类型 更新数据
     * @param rowData: 发生变更的一行数据
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        for (CanalEntry.Column colum: rowData.getBeforeColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------更新前的数据"+colum.getValue());
        }
        for (CanalEntry.Column colum: rowData.getAfterColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------更新后的数据"+colum.getValue());
        }
    }

    /**
     * @DeleteListenPoint:增加监听 监听删除前数据
     * rowData.getAfterColumList(): 适用于增加,修改
     * rowData.getBeforeColumList(): 适用于删除,修改
     * @param entryType: 当前操作的类型 删除数据
     * @param rowData: 发生变更的一行数据
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        for (CanalEntry.Column colum: rowData.getBeforeColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------删除前的数据"+colum.getValue());
        }
    }

    /**
     * 自定义监听:增加监听 监听自定义前后数据
     * rowData.getAfterColumList(): 适用于增加,修改
     * rowData.getBeforeColumList(): 适用于删除,修改
     * @param entryType: 当前操作的类型 自定义
     * @param rowData: 发生变更的一行数据
     */
    @ListenPoint(
            eventType = {CanalEntry.EventType.DELETE,CanalEntry.EventType.UPDATE},  //监听类型
            schema = "changgou_content",  //指定监听的数据库
            table = {"tb_content"},  //指定监控的表
            destination = "example"  //指定实例的地址
    )
    public void onEventCustomUpdate(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        for (CanalEntry.Column colum: rowData.getBeforeColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------自定义前的数据"+colum.getValue());
        }
        for (CanalEntry.Column colum: rowData.getAfterColumnsList()) {
            System.out.println("列名:"+colum.getName()+"-------自定义后的数据"+colum.getValue());
        }
    }
}
