package Common;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utilities {

    public static String getProjectPath() {
        // Lấy thư mục gốc của dự án (trong trường hợp này là "D:\Tester\Test2")
        Path projectPath = Paths.get("D:\\AutoTest\\test05\\src");

        // Trả về đường dẫn dướng dẫn thư mục dự án dưới dạng chuỗi
        return projectPath.toString();
    }
}
