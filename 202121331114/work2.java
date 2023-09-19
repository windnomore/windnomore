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

        System.out.print("���������ļ�·��1: ");
        String filePath1 = scanner.nextLine();

        System.out.print("���������ļ�·��2: ");
        String filePath2 = scanner.nextLine();

        long startTime = System.currentTimeMillis(); // ��ʼ��ʱ

        String paper1 = readTextFromFile(filePath1);
        String paper2 = readTextFromFile(filePath2);

        long endTime = System.currentTimeMillis(); // ������ʱ
        long fileReadTime = endTime - startTime;

        System.out.print("�������ļ�·��: ");
        String resultFilePath = scanner.nextLine();

        startTime = System.currentTimeMillis(); // ��ʼ��ʱ

        double similarity = calculateCosineSimilarity(paper1, paper2);

        endTime = System.currentTimeMillis(); // ������ʱ
        long similarityCalculationTime = endTime - startTime;

        String checkResult = getCheckResult(similarity);

        writeResultToFile(resultFilePath, checkResult);

        scanner.close();

        // ��ӡ��������
        System.out.println("�ļ���ȡ��ʱ��" + fileReadTime + " ����");
        System.out.println("���ƶȼ����ʱ��" + similarityCalculationTime + " ����");
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
            norm1 += vector1.get(word) * vector1.get(word); // ֱ�Ӽ���ƽ��
        }

        for (String word : vector2.keySet()) {
            norm2 += vector2.get(word) * vector2.get(word); // ֱ�Ӽ���ƽ��
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // ���������
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private static Map<String, Integer> createWordFrequencyVector(String text) {
        text = text.toLowerCase(); // �������ı�ת��ΪСд
        Map<String, Integer> vector = new HashMap<>();
        String[] words = text.split("\\s+|\\p{Punct}");
        for (String word : words) {
            if (!word.isEmpty()) { // ���Կ��ַ���
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
            System.err.println("��ȡ�ļ�ʱ��������: " + e.getMessage());
        }
        return content.toString();
    }

    private static String getCheckResult(double similarity) {
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedSimilarity = df.format(similarity);
        double threshold = 0.7;

        if (similarity >= threshold) {
            return "����ƪ���Ŀ��ܴ��������Ի�Ϯ��";
        } else {
            return "����ƪ���Ĳ�̫���ܴ��������Ի�Ϯ��\nԭ�������ƶ�Ϊ��" + formattedSimilarity;
        }
    }

    private static void writeResultToFile(String filePath, String result) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(result);
            System.out.println("�����д���ļ�: " + filePath);
        } catch (IOException e) {
            System.err.println("д���ļ�ʱ��������: " + e.getMessage());
        }
    }
}