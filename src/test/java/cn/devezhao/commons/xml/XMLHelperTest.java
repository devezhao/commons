package cn.devezhao.commons.xml;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class XMLHelperTest {

    private static final String PLAIN_XML =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><roottag>acunetix</roottag>";

    private static final String DOCTYPE_XML =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE roottag><roottag>acunetix</roottag>";

    @Test
    public void testCreateDocument() {
        Document doc = XMLHelper.createDocument(new StringReader(DOCTYPE_XML), "utf-8", false);
        Assert.assertNotNull(doc);
        Assert.assertEquals("acunetix", doc.getRootElement().getText());
    }

    @Test
    public void testCreateDocumentPlain() {
        Document doc = XMLHelper.createDocument(new StringReader(PLAIN_XML), "utf-8", true);
        Assert.assertEquals("acunetix", doc.getRootElement().getText());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateDocumentRejectDoctype() {
        XMLHelper.createDocument(new StringReader(DOCTYPE_XML), "utf-8", true);
    }

    @Test
    public void testParseText() {
        Assert.assertEquals("acunetix", XMLHelper.parseText(PLAIN_XML).getRootElement().getText());
    }

    @Test
    public void testParseTextWithXXE() {
        Document doc = XMLHelper.parseText(PLAIN_XML, true);
        Assert.assertEquals("acunetix", doc.getRootElement().getText());
    }

    @Test(expected = RuntimeException.class)
    public void testParseTextRejectDoctype() {
        XMLHelper.parseText(DOCTYPE_XML, true);
    }
}
