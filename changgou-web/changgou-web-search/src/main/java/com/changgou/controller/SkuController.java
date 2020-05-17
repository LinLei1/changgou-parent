package com.changgou.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 实现搜索调用
     */
    @RequestMapping(value = "/list")
    public String search(@RequestParam(required = false)Map<String,String> searchMap, Model model)throws Exception{
        //调用搜索微服务
        Map<String,Object> resultMap = skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);
        //计算分页
        Page<SkuInfo> pageInfo = new Page<SkuInfo>(
                        Long.parseLong(resultMap.get("total").toString()),
                        Integer.parseInt(resultMap.get("pageNumber").toString())+1,
                        Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("pageInfo",pageInfo);
        //将条件存储,用于页面回显数据
        model.addAttribute("searchMap",searchMap);
        //获取上次请求地址
        String[] urls = url(searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sorturl",urls[1]);
        return "search";
    }

    /**
     * 拼接组装用户请求的URL地址
     * 获取用户每次请求的地址
     * 页面需要在这次请求的地址上面添加额外的搜索条件
     */
    public String[] url(Map<String,String> searchMap){
        String url = "/search/list";  //初始化地址
        String sorturl = "/search/list";  //排序地址
        if (searchMap!=null && searchMap.size()>0){
            url+="?";
            sorturl+="?";
            for (Map.Entry<String,String> entry: searchMap.entrySet()){
                //Key是搜索的条件
                String key = entry.getKey();
                //跳过分页参数
                if (key.equalsIgnoreCase("pageNum")){
                    continue;
                }
                //value搜索的值
                String value = entry.getValue();
                url+=key+"="+value+"&";
                //排序参数,跳过
                if(key.equalsIgnoreCase("sortField") || key.equalsIgnoreCase("sortRule")){
                    continue;
                }
                sorturl+=key+"="+value+"&";
            }
            //去掉最后一个&
            url = url.substring(0,url.length()-1);
            sorturl = sorturl.substring(0,sorturl.length()-1);

        }
        return new String[]{url,sorturl};
    }
}
