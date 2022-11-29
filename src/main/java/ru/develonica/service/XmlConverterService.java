package ru.develonica.service;

import ru.develonica.model.AddressObject;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;

import static java.io.File.separator;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.time.LocalDate.parse;
import static ru.develonica.model.ObjectAttribute.*;

/**
 * Сервис по конвертации xml файла в csv файл.
 */
public class XmlConverterService {

    /** Тип элемента. */
    private static final String ELEMENT_TYPE = "OBJECT";

    /**
     * Формат заголовки csv файла.
     */
    private static final String HEADER_CSV_FILE = "%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s";

    /**
     * Формат xml файла.
     */
    private static final String XML_FORMAT = ".XML";

    /**
     * Формат csv файла.
     */
    private static final String CSV_FORMAT = ".CSV";

    /**
     * Абстрактная реализация фабрики для получения <code>XMLEventReader</code>.
     */
    private final XMLInputFactory xmlInputFactory;

    public XmlConverterService() {
        this.xmlInputFactory = XMLInputFactory.newInstance();
    }

    /**
     * Читает данные из xml файла и записывает в csv файл.
     *
     * @param xmlFile        файл для чтения
     * @param destinationDir директория назначения
     * @throws XMLStreamException    если произошла ошибка при работе с XML.
     * @throws FileNotFoundException если произошла ошибка при работе с файлом.
     */
    public void convert(File xmlFile, File destinationDir) throws XMLStreamException, FileNotFoundException {
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile));
        String csvFile = destinationDir + separator + xmlFile.getName().replace(XML_FORMAT, CSV_FORMAT);

        try (PrintWriter writer = new PrintWriter(csvFile)) {
            writer.println(format(HEADER_CSV_FILE,
                    ID.getNodeName(),
                    OBJECT_ID.getNodeName(),
                    OBJECT_GU_ID.getNodeName(),
                    CHANGE_ID.getNodeName(),
                    NAME.getNodeName(),
                    TYPE_NAME.getNodeName(),
                    LEVEL.getNodeName(),
                    OPER_TYPE_ID.getNodeName(),
                    PREV_ID.getNodeName(),
                    NEXT_ID.getNodeName(),
                    UPDATE_DATE.getNodeName(),
                    START_DATE.getNodeName(),
                    END_DATE.getNodeName(),
                    IS_ACTUAL.getNodeName(),
                    IS_ACTIVE.getNodeName()
            ));

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                AddressObject addressObject = new AddressObject();

                if (event.isStartElement()) {
                    StartElement element = event.asStartElement();

                    if (element.getName().getLocalPart().equals(ELEMENT_TYPE)) {
                        addressObject.setId(getLongValue(element, ID.getNodeName()));
                        addressObject.setObjectId(getLongValue(element, OBJECT_ID.getNodeName()));
                        addressObject.setObjectGuId(getValue(element, OBJECT_GU_ID.getNodeName()));
                        addressObject.setChangeId(getLongValue(element, CHANGE_ID.getNodeName()));
                        addressObject.setName(getValue(element, NAME.getNodeName()));
                        addressObject.setTypeName(getValue(element, TYPE_NAME.getNodeName()));
                        addressObject.setLevel(getLongValue(element, LEVEL.getNodeName()));
                        addressObject.setOperTypeId(getLongValue(element, OPER_TYPE_ID.getNodeName()));
                        addressObject.setPrevId(getLongValue(element, PREV_ID.getNodeName()));
                        addressObject.setNextId(getLongValue(element, NEXT_ID.getNodeName()));
                        addressObject.setUpdateDate(getTimeValue(element, UPDATE_DATE.getNodeName()));
                        addressObject.setStartDate(getTimeValue(element, START_DATE.getNodeName()));
                        addressObject.setEndDate(getTimeValue(element, END_DATE.getNodeName()));
                        addressObject.setIsActual(getIntegerValue(element, IS_ACTUAL.getNodeName()));
                        addressObject.setIsActive(getIntegerValue(element, IS_ACTIVE.getNodeName()));

                        writer.println(addressObject);
                    }
                }
            }
        }
    }

    private Integer getIntegerValue(StartElement element, String attribute) {
        Attribute attributeByName = element.getAttributeByName(new QName(attribute));

        return attributeByName != null
                ? valueOf(attributeByName.getValue())
                : null;
    }

    private LocalDate getTimeValue(StartElement element, String attribute) {
        Attribute attributeByName = element.getAttributeByName(new QName(attribute));

        return attributeByName != null
                ? parse(attributeByName.getValue())
                : null;
    }

    private String getValue(StartElement element, String attribute) {
        Attribute attributeByName = element.getAttributeByName(new QName(attribute));

        return attributeByName != null
                ? attributeByName.getValue()
                : null;
    }

    private Long getLongValue(StartElement element, String attribute) {
        Attribute attributeByName = element.getAttributeByName(new QName(attribute));

        return attributeByName != null
                ? Long.valueOf(attributeByName.getValue())
                : null;
    }
}
