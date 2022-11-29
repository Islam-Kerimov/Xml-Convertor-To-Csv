package ru.develonica.controller;

import org.slf4j.Logger;
import ru.develonica.service.ArgumentValidatorService;
import ru.develonica.service.XmlConverterService;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static java.io.File.separator;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class XmlConverter {

    /**
     * Logger для отображения логирования.
     */
    private static final Logger LOG = getLogger(XmlConverter.class);

    /**
     * Паттерн обработки переданного файла.
     */
    private static final String XML_FILE_PATTERN = "AS_ADDR_OBJ_[0-9].+.XML";

    /**
     * Размер пула потоков по умолчанию.
     */
    private static final int DEFAULT_POOL_SIZE = 5;

    /**
     * Первый переданный параметр.
     */
    private static final int ONE_ARGUMENT = 1;

    /**
     * Второй переданный параметр.
     */
    private static final int TWO_ARGUMENT = 2;

    /**
     * Третий переданный параметр.
     */
    private static final int THREE_ARGUMENT = 3;

    /**
     * Список всех переданных параметров.
     */
    private final String[] argument;

    /**
     * Сервис по валидации переданных аргументов.
     */
    private final ArgumentValidatorService validatorService;

    /**
     * Сервис по конвертации xml файла в csv файл.
     */
    private final XmlConverterService converterService;

    /**
     * Пул потоков.
     */
    private int poolSize;

    public XmlConverter(String[] argument,
                        ArgumentValidatorService validatorService,
                        XmlConverterService converterService) {
        this.argument = argument;
        this.validatorService = validatorService;
        this.converterService = converterService;
        this.poolSize = DEFAULT_POOL_SIZE;
    }

    public void run() {
        try {
            File xmlFile;
            File destinationDir;
            switch (argument.length) {
                case ONE_ARGUMENT -> {
                    xmlFile = validatorService.createXmlFile(argument[ONE_ARGUMENT - 1]);
                    if (xmlFile.isFile()) {
                        destinationDir = xmlFile.getParentFile();
                        convertFile(xmlFile, destinationDir);
                    } else {
                        destinationDir = xmlFile;
                        convertFileTree(xmlFile, destinationDir);
                    }
                }
                case TWO_ARGUMENT -> {
                    xmlFile = validatorService.createXmlFile(argument[ONE_ARGUMENT - 1]);
                    try {
                        poolSize = validatorService.createPoolSize(argument[TWO_ARGUMENT - 1]);
                        if (xmlFile.isFile()) {
                            destinationDir = xmlFile.getParentFile();
                            convertFile(xmlFile, destinationDir);
                        } else {
                            destinationDir = xmlFile;
                            convertFileTree(xmlFile, destinationDir);
                        }
                    } catch (NumberFormatException e) {
                        destinationDir = validatorService.createDestinationDir(argument[TWO_ARGUMENT - 1]);

                        if (xmlFile.isFile()) {
                            convertFile(xmlFile, destinationDir);
                        } else {
                            convertFileTree(xmlFile, destinationDir);
                        }
                    }
                }
                case THREE_ARGUMENT -> {
                    xmlFile = validatorService.createXmlFile(argument[ONE_ARGUMENT - 1]);
                    destinationDir = validatorService.createDestinationDir(argument[TWO_ARGUMENT - 1]);
                    poolSize = validatorService.createPoolSize(argument[THREE_ARGUMENT - 1]);
                    if (xmlFile.isFile()) {
                        convertFile(xmlFile, destinationDir);
                    } else {
                        convertFileTree(xmlFile, destinationDir);
                    }
                }
                default -> LOG.error(format("Incorrect number of arguments: %d%n", argument.length));
            }
        } catch (IOException | NumberFormatException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Запуск поиска всех файлов в файловом дереве с помощью пула потоков.
     *
     * @param directory   директория с файлами для обработки.
     * @param destination директория назначения результатов конвертации
     */
    private void convertFileTree(File directory, File destination) {
        File[] files = directory.listFiles();
        if (files != null) {
            ExecutorService threadPool = newFixedThreadPool(poolSize);

            stream(files).forEach(file -> threadPool.execute(() -> searchFiles(file, destination)));

            threadPool.shutdown();
        }
    }

    /**
     * Обработка все файлов в файловом дереве с помощью рекурсии.
     *
     * @param folder      файл или директория для обработки
     * @param destination директория назначения результатов конвертации
     */
    private void searchFiles(File folder, File destination) {
        if (folder.isFile()) {
            convertFile(folder, destination.getAbsoluteFile());
        } else {
            File[] files = folder.listFiles();
            if (files != null) {
                stream(files).forEach(file -> {
                    File parentDir = new File(
                            destination + separator + file.getParentFile().getName());
                    if (!parentDir.exists()) {
                        parentDir.mkdir();
                    }
                    searchFiles(file, parentDir);
                });
            }
        }
    }

    /**
     * Конвертация файла xml формата в csv формат.
     *
     * @param file        файл для конвертации
     * @param destination директория назначения результатов конвертации
     */
    private void convertFile(File file, File destination) {
        if (!file.getName().matches(XML_FILE_PATTERN)) {
            LOG.warn(format("File NOT match the pattern [AS_ADDR_OBJ_ХХХ] - [%s]", file.getName()));
        } else {
            LOG.info(format("File match the pattern [AS_ADDR_OBJ_ХХХ] - [%s]", file.getName()));

            try {
                converterService.convert(file, destination);
                LOG.info(format("File was convert successfully - [%s]", file.getName()));
            } catch (XMLStreamException xse) {
                LOG.error("Error while working with XML", xse);
            } catch (FileNotFoundException fnfe) {
                LOG.error("I/O error", fnfe);
            }
        }
    }
}
