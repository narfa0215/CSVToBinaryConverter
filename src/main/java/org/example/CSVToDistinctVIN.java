package org.example;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CSVToDistinctVIN {

    public static void main(String[] args) {
        String inputFileName = "D:\\Downloads\\jinan_202403191608_send.csv";
        String outputFileName = "D:\\Downloads\\jinan_202403191608_send_vin.csv";

        try {
            // 创建文件读取流
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            // 创建文件写入流
            BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(Paths.get(outputFileName)));

            Set<String> vinSet = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {
                // 将读取的十六进制字符串转换为字节数组
                byte[] bytes = hexStringToByteArray(line);
                // 将字节数组写入二进制文件
                if (bytes != null) {
                    byte[] vinCharArray = new byte[17];
                    System.arraycopy(bytes, 4, vinCharArray, 0, vinCharArray.length);
                    vinSet.add(new String(vinCharArray));
                }
            }

            for (String vin : vinSet) {
                writer.write(vin.getBytes());
                writer.write('\r');
                writer.write('\n');
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
