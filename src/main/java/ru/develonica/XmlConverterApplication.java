package ru.develonica;

import ru.develonica.controller.XmlConverter;
import ru.develonica.service.ArgumentValidatorService;
import ru.develonica.service.XmlConverterService;

/**
 * Точка запуска приложения.
 */
public class XmlConverterApplication {

    public static void main(String[] args) {
        ArgumentValidatorService validatorService = new ArgumentValidatorService();
        XmlConverterService converterService = new XmlConverterService();

        XmlConverter controller = new XmlConverter(args, validatorService, converterService);
        controller.run();
    }
}
