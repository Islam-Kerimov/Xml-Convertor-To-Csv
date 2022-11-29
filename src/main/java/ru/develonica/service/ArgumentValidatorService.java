package ru.develonica.service;

import java.io.File;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * Сервис по валидации переданных аргументов.
 */
public class ArgumentValidatorService {

    private static final String INCORRECT_XML_FILE = "Argument '%s' is not file or directory";
    private static final String INCORRECT_DIRECTORY = "Argument '%s' is not directory";

    /**
     * Преобразовывает переданный параметр в файл.
     *
     * @param arg переданный пользователем параметр
     * @return файл
     * @throws IOException если переданный параметр не файл
     */
    public File createXmlFile(String arg) throws IOException {
        File xmlFile = new File(arg);

        if (!xmlFile.isFile() && !xmlFile.isDirectory()) {
            throw new IOException(format(INCORRECT_XML_FILE, arg));
        }

        return xmlFile;
    }

    /**
     * Преобразовывает переданный параметр в директорию.
     *
     * @param arg переданный пользователем параметр
     * @return директорию
     * @throws IOException если переданный параметр не директория
     */
    public File createDestinationDir(String arg) throws IOException {
        File destinationDir = new File(arg);

        if (!destinationDir.exists()) {
            destinationDir.mkdir();
        }

        if (!destinationDir.isDirectory()) {
            throw new IOException(format(INCORRECT_DIRECTORY, arg));
        }

        return destinationDir;
    }

    /**
     * Преобразовывает переданный параметр в число.
     *
     * @param arg переданный пользователем параметр
     * @return размер потоков.
     */
    public int createPoolSize(String arg) {
        try {
            return parseInt(arg);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.toString());
        }
    }
}
