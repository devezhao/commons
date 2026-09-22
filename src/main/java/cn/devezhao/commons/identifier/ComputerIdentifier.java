package cn.devezhao.commons.identifier;

import cn.devezhao.commons.EncryptUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.nio.charset.StandardCharsets;

/**
 * 基于硬件信息（厂商、主板序列号、处理器标识、逻辑处理器数）生成机器标识。
 * 注意 {@link #generateIdentifierKey()} 使用 CRC32，不具备抗碰撞与抗伪造能力，不能用于安全场景
 *
 * @author devezhao
 * @since 2022/1/18
 */
public class ComputerIdentifier {

    /**
     * @return 形如 {@code vendor#serial#processorIdentifier#processors} 的原始机器标识
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
     * @return 机器标识的 CRC32 十六进制表示（8 位）
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
