package org.example;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVToBinaryConverter {

    public static void main(String[] args) {
        String inputFileName = "D:\\Test\\input_hex.csv";
        String outputFileName = "D:\\Test\\output_binary.bin";

        try {
            // 创建文件读取流
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            // 创建文件写入流
            BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(Paths.get(outputFileName)));

            boolean isFoundFirstRow = false;

            int count = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                if (!isFoundFirstRow) {
                    isFoundFirstRow = true;
                    continue;
                }
                // 将读取的十六进制字符串转换为字节数组
                byte[] bytes = hexStringToByteArray("2323" + line.substring(1, line.length() - 1));
                // 将字节数组写入二进制文件
                if (bytes != null) {
                    byte code = 0x00;
                    for (int i = 2; i < bytes.length - 1; i++) {
                        if (i == 2) {
                            code = bytes[i];
                            continue;
                        }
                        code ^= bytes[i];
                    }
                    if (code == bytes[bytes.length - 1]) {
                        if (count >= 100000) {
                            break;
                        }
                        writer.write(bytes);
                        count++;
                    }
                }
            }

            // 关闭流
            reader.close();
            writer.close();

            System.out.println("转换完成。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int charIntegerToHexInteger(int charInteger) {
        if (charInteger >= 'a' && charInteger <= 'f') {
            return charInteger - 'a' + 10;
        }
        if (charInteger >= 'A' && charInteger <= 'F') {
            return charInteger - 'A' + 10;
        }
        if (charInteger >= '0' && charInteger <= '9') {
            return charInteger - '0';
        }
        return -1;
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        if (length % 2 != 0) {
            // 输入长度必须为偶数
            return null;
        }

        byte[] byteArray = new byte[length / 2];
        char[] charArray = hexString.toCharArray();

        for (int i = 0, j = 0; i < length; i += 2, j++) {
            int highHalfByteHexInteger = charIntegerToHexInteger(charArray[i]);
            int lowHalfByteHexInteger = charIntegerToHexInteger(charArray[i + 1]);
            if (highHalfByteHexInteger == -1 || lowHalfByteHexInteger == -1) {
                return null;
            }
            byteArray[j] = (byte) (highHalfByteHexInteger << 4 | lowHalfByteHexInteger);
        }
        return byteArray;
    }

}
