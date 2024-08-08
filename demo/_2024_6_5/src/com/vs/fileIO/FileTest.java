package com.vs.fileIO;
import java.io.*;
import java.util.*;

public class FileTest {
    public void test() throws IOException {
        // 获取文件列表
//        File filePath = new File("D:\\Code\\java\\_2024_6_5");
//        File[] files = filePath.listFiles();
//        for(File f: files) {
//            System.out.println(f);
//        }
        // 数据流(若文件不存在，则会先创建文件，清空文件再写入)
        FileOutputStream s = new FileOutputStream("D:\\Code\\java\\_2024_6_5\\test.txt");
        s.write(78);
        s.close();
    }

    public void testDeepCopy(String srcDir, String destDir) throws IOException{
        // 创建文件对象进行流操作
        File src = new File(srcDir);
        File dest = new File(destDir);

        // 递归拷贝
        recursive_copy(src, dest);

        // test read
//        FileInputStream fs = new FileInputStream(destDir + "\\man.txt");
//        int len;
//        while((len = fs.read()) != -1) {
//            System.out.println((char) len);
//        }
    }

    void recursive_copy(File src, File dest) throws IOException{
        // 判断目标目录是否存在
        if(!dest.exists()) dest.mkdirs();

        // 获取src下所有文件
        File[] files = src.listFiles();
        for(File file: files) {
            if(file.isFile()) {
                // 通过遍历到的当前文件，拷贝给新创建的文件
                FileInputStream fs = new FileInputStream(file);
                FileOutputStream os = new FileOutputStream(new File(dest, file.getName()));
                byte[] bytes = new byte[1024];
                int len;
                // 通过输出流将读取到的len字节存入bytes数组缓冲区中，读满或最后一次未读满统一写到os输出流对应的文件中
                while((len = fs.read(bytes)) != -1) {
                    os.write(bytes, 0, len);
                }
                // 读写完毕，关闭输入输出字节流
                os.close();
                fs.close();
            } else {
                // 目录递归拷贝
                recursive_copy(file, new File(dest, file.getName()));
            }
        }
    }

    public void testSort(String src, String dest) throws IOException{
        // 读取文件内容
        FileReader reader = new FileReader(src);
        int ch;
        // 将文件内容去除-后，以数值形式存入列表中
        ArrayList<Integer> arr = new ArrayList<>();
        while((ch = reader.read()) != -1) {
            if(ch >= 48 && ch <= 57) arr.add((int)ch - 48);
        }
        // 排降序
        Collections.sort(arr, Comparator.reverseOrder());
//        System.out.println(arr);
        // 将数值转字符串后以特定格式拼接，并写入目标文件
        FileWriter fw = new FileWriter(dest + "\\desc.txt");
        StringJoiner sj = new StringJoiner("-");
        Iterator<Integer> it = arr.iterator();
        while(it.hasNext()) {
            sj.add(it.next().toString());
        }
        // 写入文件，并关闭文件流
        fw.write(sj.toString());
        fw.close();
        reader.close();
    }
}
