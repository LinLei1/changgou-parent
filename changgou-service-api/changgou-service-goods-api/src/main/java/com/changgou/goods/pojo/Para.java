package com.changgou.goods.pojo;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:传智播客
 * @Description:Para构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_para")
public class Para implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//主键

    @Column(name = "name")
	private String name;//名称

    @Column(name = "options")
	private String options;//选项

    @Column(name = "seq")
	private Integer seq;//排序

    @Column(name = "template_id")
	private Integer templateId;//模板ID



	//get方法
	public Long getId() {
		return id;
	}

	//set方法
	public void setId(Long id) {
		this.id = id;
	}
	//get方法
	public String getName() {
		return name;
	}

	//set方法
	public void setName(String name) {
		this.name = name;
	}
	//get方法
	public String getOptions() {
		return options;
	}

	//set方法
	public void setOptions(String options) {
		this.options = options;
	}
	//get方法
	public Integer getSeq() {
		return seq;
	}

	//set方法
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	//get方法
	public Integer getTemplateId() {
		return templateId;
	}

	//set方法
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}


}
