/*
 Copyright (C) 2011 QIDAPP.com. All rights reserved.
 QIDAPP.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.devezhao.commons.runtime;

/**
 * 内存信息
 * 
 * @author Zhao Fangfang
 * @version $Id: MemoryInformation.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public interface MemoryInformation {

	/**
	 * @return
	 */
	String getName();
	
	/**
	 * @return
	 */
	long getTotal();
	
	/**
	 * @return
	 */
	long getUsed();
	
	/**
	 * @return
	 */
	long getFree();
}
