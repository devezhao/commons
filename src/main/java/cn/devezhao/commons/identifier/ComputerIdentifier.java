package cn.devezhao.commons.identifier;

import cn.devezhao.commons.EncryptUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.nio.charset.StandardCharsets;

/**
 * @author devezhao
 * @since 2022/1/18
 */
public class ComputerIdentifier {

    /**
     * @return
     */
    public static String generateIdentifier() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        String vendor = operatingSystem.getManufacturer();
        String processorSerialNumber = computerSystem.getSerialNumber();
        String processorIdentifier = centralProcessor.getProcessorIdentifier().getIdentifier();
        int processors = centralProcessor.getLogicalProcessorCount();

        String delimiter = "#";

        return vendor +
                delimiter +
                processorSerialNumber +
                delimiter +
                processorIdentifier +
                delimiter +
                processors;
    }

    /**
     * @return
     */
    public static String generateIdentifierKey() {
        String id = generateIdentifier();
        return EncryptUtils.toCRC32HexPadding(id.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String id = generateIdentifier();
            System.out.println(id + " = " + generateIdentifierKey());
        }
    }
}
