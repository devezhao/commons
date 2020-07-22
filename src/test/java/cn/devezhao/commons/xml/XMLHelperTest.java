package cn.devezhao.commons.xml;

import junit.framework.TestCase;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class XMLHelperTest extends TestCase {

    @Test
    public void testCreateDocument() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<!DOCTYPE roottag PUBLIC \"-//VSR//PENTEST//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">" +
                "<roottag>acunetix</roottag>";

        XMLHelper.createDocument(new StringReader(xml), "utf-8", false);
        XMLHelper.createDocument(new StringReader(xml), "utf-8", true);
    }
}