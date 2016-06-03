package com.test.boatstagram.utils;

import java.io.File;

public class FileUtils {

    public static File getFileWithUniqueFileName(File directory, String fileNameWithoutExtensionAndDot, String extensionWithDot) {
        if (!directory.isDirectory() && directory.exists())
            return null;
        if (fileNameWithoutExtensionAndDot == null)
            fileNameWithoutExtensionAndDot = "";
        if (extensionWithDot == null)
            extensionWithDot = "";

        File file = null;
        if (!fileNameWithoutExtensionAndDot.equals("") && !extensionWithDot.equals("")) {
            file = new File(directory, fileNameWithoutExtensionAndDot + extensionWithDot);
            if (!file.exists())
                return file;
        }

        int i = 1;
        while ((file = new File(directory, fileNameWithoutExtensionAndDot + " (" + i + ")" + extensionWithDot)).exists())
            i++;
        return file;
    }

}
