package com.gxx.jiagu.plugin.utils

class Utils {

    /**
     * 将字符串的首字母转大写
     */
    static String capitalize(String str) {
        if (isBlank(str)) return ""
        return str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0
    }

    static boolean isBlank(CharSequence s) {
        if (s == null) {
            return true
        } else {
            for (int i = 0; i < s.length(); ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false
                }
            }
            return true
        }
    }

    static String readText(InputStreamReader reader) {
        StringWriter stringWriter = new StringWriter()
        long charsCopied = 0
        char[] buffer = new char[8 * 1024]
        int chars = reader.read(buffer)
        while (chars >= 0) {
            stringWriter.write(buffer, 0, chars)
            charsCopied += chars
            chars = reader.read(buffer)
        }
        return stringWriter.toString()
    }

    /**
     * 执行命令
     * @param command
     * @return
     */
    static String exec(String command) {
        StringBuilder resultBuilder = new StringBuilder();
        Process pro = null;
        BufferedReader input = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            throw new NullPointerException("reinforce task failed,Runtime is null");
        }
        try {
            pro = runTime.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream(),"GBK"));
            String line;
            while ((line = input.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pro != null) {
                pro.destroy();
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String returnString = resultBuilder.toString();
        String tmp = """
################################################
#                                              #
#        ## #   #    ## ### ### ##  ###        #
#       # # #   #   # #  #  # # # #  #         #
#       ### #   #   ###  #  # # ##   #         #
#       # # ### ### # #  #  ### # # ###        #
#                                              #
# Obfuscation by Allatori Obfuscator v5.6 DEMO #
#                                              #
#           http://www.allatori.com            #
#                                              #
################################################
        """.trim()
        returnString = returnString.replace(tmp, "").trim()
        return returnString;
    }


}