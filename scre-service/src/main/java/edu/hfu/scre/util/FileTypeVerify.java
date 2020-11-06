package edu.hfu.scre.util;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 验证文件类型，通过流
 * @author dongmei.gao
 * @date 2020/7/7 15:48
 */
public class FileTypeVerify {
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    private static FileInputStream is;

    static {
        getAllFileType(); // 初始化文件类型信息
    }

    /**
     * 常用文件格式
     */
    private static void getAllFileType() {
        /** JPEG (jpg) */
        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg.jpeg");
        /** PNG (png) */
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png");

        /** MS Excel 注意：word、msi 和 excel的文件头一样 */
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc.xls");
        /** docx文件或xlsx */
        FILE_TYPE_MAP.put("504b0304", "docx.xlsx.zip");
       /** Adobe Acrobat (pdf) */
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf");
        /** Quicken (qdf) */
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf");

        FILE_TYPE_MAP.put("52617221", "rar");

        FILE_TYPE_MAP.put("null", null);
    }

        /**
         * 得到上传文件的文件头
         * @param src
         * @return
         */

        public static String bytesToHexString(byte[] src) {
            StringBuilder stringBuilder = new StringBuilder();
            if (src == null || src.length <= 0) {
                return null;
            }

            for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        }

    /**

     * 根据制定文件的文件头判断其文件类型，验证文件头前20位
     * @param b  文件的二进制流
     * @return
     */
    public static String getFileType(byte[] b) {
        String res = null;
        try {
            String fileCode = bytesToHexString(b);
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();

            while (keyIter.hasNext()) {
                String key = keyIter.next();
                // 验证前8个字符比较
                int fileCodeLen = fileCode.toLowerCase().length() ;
                int endIndex = fileCodeLen >8 ? 8 : fileCodeLen;
                if (key.toLowerCase().startsWith(fileCode.toLowerCase().substring(0, endIndex))
                        || fileCode.toLowerCase().substring(0, endIndex).startsWith(key.toLowerCase())) {
                    res = FILE_TYPE_MAP.get(key);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static void main(String[] args) throws Exception {
        String filePaht = "C:\\Users\\lenovo\\Downloads\\关于2020年度下半年课题结题验收相应表格.zip" ;
        is = new FileInputStream(filePaht);
        byte[] b = new byte[10];
        is.read(b, 0, b.length);

        // jpg, pdf doc\xls，docx , xlsx,png 验证通过
        String type = getFileType(b);

        System.out.println("Except : " + type);
    }
}
