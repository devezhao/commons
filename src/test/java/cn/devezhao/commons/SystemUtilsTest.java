package cn.devezhao.commons;

import junit.framework.TestCase;
import org.junit.Test;

public class SystemUtilsTest extends TestCase {

    @Test
    public void testGetRuntimeInformation() {
        System.out.println("getJvmInputArguments " + SystemUtils.getRuntimeInformation().getJvmInputArguments());
        System.out.println("getSystemLoad " + SystemUtils.getRuntimeInformation().getSystemLoad());
        System.out.println("getProcessLoad " + SystemUtils.getRuntimeInformation().getProcessLoad());
        System.out.println("getServletPort " + SystemUtils.getRuntimeInformation().getServletPort());
        System.out.println("getTotalHeapMemory " + SystemUtils.getRuntimeInformation().getTotalHeapMemory());
    }
}