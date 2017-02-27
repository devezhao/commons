package cn.devezhao.commons.identifier;

import java.io.Serializable;

/**
 * Identifier generator
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/13/08
 * @version $Id: IdentifierGenerator.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface IdentifierGenerator {

	/**
	 * Generate a new identifier
	 * 
	 * @return
	 */
	Serializable generate();
	
	/**
	 * Gets length of id
	 * 
	 * @return
	 */
	int getLength();
}
