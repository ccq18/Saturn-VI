package ccq18.saturn.vi.demo3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateOBj {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 500; i++) {
            if (Math.random() > 0.4) {
                writeclass("ccq18.saturn.vi.demo3.demo.d4","Demo" + i, true);
            } else {
                writeclass("ccq18.saturn.vi.demo3.demo.d4","Demo" + i, false);
            }

        }
    }

    public static void writeclass(String packageN, String className, boolean anno) throws IOException {
        String classcontent = "";
        if (anno) {
            classcontent = "package " + packageN + ";\n" +
                    "\n" +
                    "import ccq18.saturn.vi.demo3.annos.Controller;\n" +
                    "@Controller\n" +
                    "public class " + className + "  {\n" +
                    "    \n" +
                    "}";
        } else {
            classcontent = "package " + packageN + ";\n" +
                    "\n" +
                    "public class " + className + "  {\n" +
                    "    \n" +
                    "}\n";
        }

        writefile("/Users/mac/code/saturn-vi/data/" + className + ".java", classcontent);

    }

    public static void writefile(String file, String str) throws IOException {
        File writename = new File(file);
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(str); // \r\n即为换行
        out.flush(); // 把缓存区内容压入文件
        out.close();

    }
}
