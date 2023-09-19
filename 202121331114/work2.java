import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.text.DecimalFormat;

public class work2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("输入论文文件路径1: ");
        String filePath1 = scanner.nextLine();

        System.out.print("输入论文文件路径2: ");
        String filePath2 = scanner.nextLine();

        long startTime = System.currentTimeMillis(); // 开始计时

        String paper1 = readTextFromFile(filePath1);
        String paper2 = readTextFromFile(filePath2);

        long endTime = System.currentTimeMillis(); // 结束计时
        long fileReadTime = endTime - startTime;

        System.out.print("输出结果文件路径: ");
        String resultFilePath = scanner.nextLine();

        startTime = System.currentTimeMillis(); // 开始计时

        double similarity = calculateCosineSimilarity(paper1, paper2);

        endTime = System.currentTimeMillis(); // 结束计时
        long similarityCalculationTime = endTime - startTime;

        String checkResult = getCheckResult(similarity);

        writeResultToFile(resultFilePath, checkResult);

        scanner.close();

        // 打印性能数据
        System.out.println("文件读取耗时：" + fileReadTime + " 毫秒");
        System.out.println("相似度计算耗时：" + similarityCalculationTime + " 毫秒");
    }

    private static double calculateCosineSimilarity(String text1, String text2) {
        Map<String, Integer> vector1 = createWordFrequencyVector(text1);
        Map<String, Integer> vector2 = createWordFrequencyVector(text2);

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : vector1.keySet()) {
            if (vector2.containsKey(word)) {
                dotProduct += vector1.get(word) * vector2.get(word);
            }
            norm1 += vector1.get(word) * vector1.get(word); // 直接计算平方
        }

        for (String word : vector2.keySet()) {
            norm2 += vector2.get(word) * vector2.get(word); // 直接计算平方
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // 避免除以零
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private static Map<String, Integer> createWordFrequencyVector(String text) {
        text = text.toLowerCase(); // 将整个文本转换为小写
        Map<String, Integer> vector = new HashMap<>();
        String[] words = text.split("\\s+|\\p{Punct}");
        for (String word : words) {
            if (!word.isEmpty()) { // 忽略空字符串
                vector.put(word, vector.getOrDefault(word, 0) + 1);
            }
        }
        return vector;
    }

    private static String readTextFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("读取文件时发生错误: " + e.getMessage());
        }
        return content.toString();
    }

    private static String getCheckResult(double similarity) {
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedSimilarity = df.format(similarity);
        double threshold = 0.7;

        if (similarity >= threshold) {
            return "这两篇论文可能存在相似性或抄袭！";
        } else {
            return "这两篇论文不太可能存在相似性或抄袭。\n原因是相似度为：" + formattedSimilarity;
        }
    }

    private static void writeResultToFile(String filePath, String result) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(result);
            System.out.println("结果已写入文件: " + filePath);
        } catch (IOException e) {
            System.err.println("写入文件时发生错误: " + e.getMessage());
        }
    }
}