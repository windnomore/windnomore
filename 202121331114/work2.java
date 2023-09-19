import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.text.DecimalFormat;

public class work2 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("input paper path1:");
        String filepath_ip1 = scanner.nextLine();
        System.out.println("input paper path2:");
        String filepath_ip2 = scanner.nextLine();
        String paper1 = readTextFromFile(filepath_ip1);
        String paper2 = readTextFromFile(filepath_ip2);
        System.out.println("output result path:");
        String result = scanner.nextLine();

        Map<String, Integer> vector1 = createWordFrequencyVector(paper1);
        Map<String, Integer> vector2 = createWordFrequencyVector(paper2);

        double similarity = calculateCosineSimilarity(vector1, vector2);

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedSimilarity = df.format(similarity);

        double threshold = 0.7;

        String check_result = (similarity >= threshold) ? "这两篇论文可能存在相似性或抄袭！" : "这两篇论文不太可能存在相似性或抄袭。"+"\n原因是相似度为："+formattedSimilarity;
        result = result+"result.txt";
        writeResultToFile(result, check_result);
        scanner.close();

    }

    private static double calculateCosineSimilarity(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : vector1.keySet()) {
            if (vector2.containsKey(word)) {
                dotProduct += vector1.get(word) * vector2.get(word);
            }
            norm1 += Math.pow(vector1.get(word), 2);
        }

        for (String word : vector2.keySet()) {
            norm2 += Math.pow(vector2.get(word), 2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // 避免除以零
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private static Map<String, Integer> createWordFrequencyVector(String text) {
        Map<String, Integer> vector = new HashMap<>();
        String[] words = text.split("\\s+"); // 使用空格分割文本
        for (String word : words) {
            word = word.toLowerCase(); // 转换为小写以忽略大小写
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }
        return vector;
    }
    private static String readTextFromFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    private static void writeResultToFile(String filePath, String result) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(result);
            System.out.println("结果已写入文件: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
